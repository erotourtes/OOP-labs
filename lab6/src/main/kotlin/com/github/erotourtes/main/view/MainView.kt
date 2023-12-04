package com.github.erotourtes.main.view

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.utils.*
import javafx.stage.StageStyle
import tornadofx.*
import java.io.File

class MainController : Controller() {
    private var state = MainState()
    private val model: MainModel by inject()
    private var pSender: ProcessSender? = null
    private var pOutReceiver: ProcessReceiver? = null

    init {
        model.item = state
    }

    init {
        Runtime.getRuntime().addShutdownHook(Thread {
            dispose()
        })
    }

    fun showDialog() {
        val dialog = find<DialogView>()
        dialog.openModal(stageStyle = StageStyle.UTILITY)
    }

    fun send() {
        if (pSender == null || !pSender!!.isAlive) initChildProcess()

        pSender?.writeMessage(state.toString())
        logger("MainApp(write): $state")
    }

    private fun initChildProcess() {
        logger("MainApp(CHILDPROCESS)")
        val path =
            "/home/sirmax/Files/Documents/projects/kotlin/oop-labs-creating-last-step/lab6/out/artifacts/First_jar/tornadofx-maven-project.jar"
        runJarFile(File(path))?.let {
            pSender?.close()
            pSender = ProcessSender(it)
            pOutReceiver = ProcessReceiver(it)
        }
    }

    fun dispose() {
        pSender?.let {
            logger("MainApp(write): $DESTROY")
            it.writeMessage(DESTROY)
            it.process.destroy()
            it.close()
        }
        pOutReceiver?.close()
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