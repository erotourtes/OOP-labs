package com.github.erotourtes.main.view

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.utils.DESTROY
import com.github.erotourtes.utils.ProcessSender
import com.github.erotourtes.utils.runJarFile
import javafx.stage.StageStyle
import javafx.stage.Window
import tornadofx.*
import java.io.File

class MainController : Controller() {
    private var state = MainState()
    private val model: MainModel by inject()
    private var pc: ProcessSender? = null

    init {
        model.item = state
    }

    fun showDialog() {
        val dialog = find<DialogView>()
        dialog.openModal(stageStyle = StageStyle.UTILITY)
    }

    fun runJar(window: Window?) {
        if (pc == null) {
            val path = "/home/sirmax/Files/Documents/projects/kotlin/oop-labs-creating-last-step/lab6/out/artifacts/First_jar/tornadofx-maven-project.jar"
            val file = File(path)
//            val file = FileChooser().apply {
//                title = "Select jar file"
//                extensionFilters.add(FileChooser.ExtensionFilter("Jar files", "*.jar"))
//            }.showOpenDialog(window) ?: return

            runJarFile(file)?.let {
                pc = ProcessSender(it)
            }
        }

        pc?.writeMessage(state.toString())
    }

    fun dispose() {
        pc?.let {
            it.writeMessage(DESTROY)
            it.close()
            it.process.destroy()
        }
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
                    ctrl.runJar(this@MainView.currentWindow)
                }
            }
        }
    }
}