package com.github.erotourtes.main.view

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.utils.*
import com.github.erotourtes.utils.EventEmitter
import com.github.erotourtes.utils.process_stream.ProcessReceiver
import com.github.erotourtes.utils.process_stream.ProcessSender
import javafx.geometry.Pos
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File

class ProcessState(
    private val process: Process,
    val sender: ProcessSender,
    receiver: ProcessReceiver,
) {
    val alive get() = isAlive && process.isAlive
    val ee = EventEmitter(receiver)

    private var isAlive: Boolean = true
    private var isDestroyedMsgReceived: Boolean = false

    init {
        ee.subscribe(MessageType.ON_DESTROY) { isDestroyedMsgReceived = true }
    }

    fun close() {
        runCatching {
            sender.send(MessageType.DESTROY)
            // To see the logs
            while (!isDestroyedMsgReceived) Thread.sleep(PROCESS_UPDATE_TIME)
            Logger.log("process has closed ${process.isAlive}")
            sender.close()
            ee.close()
            process.destroy()
            isAlive = false
        }.onFailure {
            Logger.log("ProcessState(close): ${it.message}", Logger.InfoType.ERROR)
        }
    }
}

class MainController : Controller() {
    private var state = MainState()
    private val model: MainModel by inject()
    private var program1: ProcessState? = null
    private var program2: ProcessState? = null

    init {
        model.item = state
    }

    fun showDialog() {
        val dialog = find<DialogView>()
        dialog.openModal(stageStyle = StageStyle.UTILITY)
    }

    fun send() {
        if (!isAlive(program1)) initChildProcessProgram1()
        program1!!.sender.send(MessageType.DATA, state.toString())
    }

    private fun initChildProcessProgram1() {
        Logger.log("child process 1 init")
        val path =
            "/home/sirmax/Files/Documents/projects/kotlin/oop-labs-creating-last-step/lab6/out/artifacts/First_jar/tornadofx-maven-project.jar"
        runJarFile(File(path))?.let {
            program1?.close()
            program1 = ProcessState(it, ProcessSender(it, EventEmitter.getFormatter()), ProcessReceiver(it))
        }

        program1!!.ee.subscribe(MessageType.DATA) {
            val items = ListConverter.toList<Double>(it, String::toDouble)
            if (items.isEmpty()) return@subscribe

            if (!isAlive(program2)) initChildProcessProgram2()
            program2!!.sender.send(MessageType.DATA, ListConverter.toString(items))
        }
    }

    private fun initChildProcessProgram2() {
        Logger.log("child process 2 init")
        val path =
            "/home/sirmax/Files/Documents/projects/kotlin/oop-labs-creating-last-step/lab6/out/artifacts/Second_jar/tornadofx-maven-project.jar"
        runJarFile(File(path))?.let {
            program2?.close()
            program2 = ProcessState(it, ProcessSender(it, EventEmitter.getFormatter()), ProcessReceiver(it))
        }
    }

    fun dispose() {
        Logger.log("dispose")
        program1?.close()
        program2?.close()
    }

    private fun isAlive(program: ProcessState?) = program?.alive ?: false
}

class MainView : View("Main") {
    private val ctrl: MainController by inject()

    override val root = vbox {
        menubar {
            menu("Lab") {
                item("Show dialog").action(ctrl::showDialog)
            }
        }

        borderpane {
            center = vbox {
                alignment = Pos.BASELINE_CENTER
                button("RUN").action {
                    ctrl.send()
                }
            }
        }
    }
}