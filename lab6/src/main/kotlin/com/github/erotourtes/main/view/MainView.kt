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
import kotlin.reflect.KMutableProperty0

class MainController : Controller(), Closable {
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

    override fun close() {
        Logger.log("dispose")
        program1?.close()
        program2?.close()
    }

    private fun initChildProcessProgram1() {
        Logger.log("child process 1 init")
        val path =
            "/home/sirmax/Files/Documents/projects/kotlin/oop-labs-creating-last-step/lab6/out/artifacts/First_jar/tornadofx-maven-project.jar"

        runJar(File(path), ::program1)

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
        runJar(File(path), ::program2)
    }

    private fun isAlive(program: ProcessState?) = program?.alive ?: false

    private fun runJar(file: File, property: KMutableProperty0<ProcessState?>) {
        runJarFile(file)?.let {
            property.get()?.close()
            property.set(ProcessState(it, ProcessSender(it, EventEmitter.getFormatter()), ProcessReceiver(it)))
        }
    }
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