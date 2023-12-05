package com.github.erotourtes.main.view

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.utils.*
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File

data class ProcessState(
    val process: Process,
    val sender: ProcessSender,
    val receiver: ProcessReceiver
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

    init {
        model.item = state
    }

//    init {
//        Runtime.getRuntime().addShutdownHook(Thread {
//            dispose()
//        })
//    }

    fun showDialog() {
        val dialog = find<DialogView>()
        dialog.openModal(stageStyle = StageStyle.UTILITY)
    }

    fun send() {
        if (program1 == null || !program1!!.alive) initChildProcess()
        program1!!.sender.writeMessage(state.toString())
    }

    private fun initChildProcess() {
        Logger.log("child process init")
        val path =
            "/home/sirmax/Files/Documents/projects/kotlin/oop-labs-creating-last-step/lab6/out/artifacts/First_jar/tornadofx-maven-project.jar"
        runJarFile(File(path))?.let {
            program1?.close()
            program1 = ProcessState(it, ProcessSender(it), ProcessReceiver(it))
        }
    }

    fun dispose() {
        Logger.log("dispose")
        program1?.close()
    }
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