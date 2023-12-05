package com.github.erotourtes.main.view

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.utils.*
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File

data class ProcessState(
    val process: Process, val sender: ProcessSender, val receiver: ProcessReceiver
) {
    private var isAlive: Boolean = true
    val alive get() = isAlive && process.isAlive

    fun close() {
        try {
            sender.writeMessage(DESTROY)
            Thread.sleep(1000) // to see process input
            sender.close()
            receiver.close()
            process.destroy()
            isAlive = false
        } catch (e: Exception) {
            Logger.log("ProcessState(close): $e", Logger.InfoType.ERROR)
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
        program1!!.sender.writeMessage(state.toString())
    }

    private fun initChildProcessProgram1() {
        Logger.log("child process 1 init")
        val path =
            "/home/sirmax/Files/Documents/projects/kotlin/oop-labs-creating-last-step/lab6/out/artifacts/First_jar/tornadofx-maven-project.jar"
        runJarFile(File(path))?.let {
            program1?.close()
            program1 = ProcessState(it, ProcessSender(it), ProcessReceiver(it))
        }

        program1!!.receiver.inputData.addListener { _, _, newMsg ->
            if (newMsg == EMPTY) return@addListener
            if (newMsg.isEmpty()) return@addListener

            val items = ListConverter.toList<Double>(newMsg, String::toDouble)
            Logger.log(items.toString(), Logger.InfoType.WARNING)
            if (!isAlive(program2)) initChildProcessProgram2()
            program2!!.sender.writeMessage(ListConverter.toString(items))
        }
    }

    private fun initChildProcessProgram2() {
        Logger.log("child process 2 init")
        val path =
            "/home/sirmax/Files/Documents/projects/kotlin/oop-labs-creating-last-step/lab6/out/artifacts/Second_jar/tornadofx-maven-project.jar"
        runJarFile(File(path))?.let {
            program2?.close()
            program2 = ProcessState(it, ProcessSender(it), ProcessReceiver(it))
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
    private val model: MainModel by inject()

    override val root = vbox {
        menubar {
            menu("Lab") {
                item("Show dialog").action(ctrl::showDialog)
            }
        }

        borderpane {
            left = vbox {
                label("n") {
                    bind(model.nProp.stringBinding { "n = $it" })
                }
                label("min") {
                    bind(model.minProp.stringBinding { "min = $it" })
                }
                label("max") {
                    bind(model.maxProp.stringBinding { "max = $it" })
                }
            }

            center = vbox {
                button("RUN").action {
                    ctrl.send()
                }
            }
        }
    }
}