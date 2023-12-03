package com.github.erotourtes.main.view

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import com.github.erotourtes.process_communicators.ProcessSender
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import javafx.stage.Window
import tornadofx.*

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
            val file = FileChooser().apply {
                title = "Select jar file"
                extensionFilters.add(FileChooser.ExtensionFilter("Jar files", "*.jar"))
            }.showOpenDialog(window) ?: return

            val java = "${System.getProperty("java.home")}/bin/java"
            val path = file.absolutePath
            val process = Runtime.getRuntime().exec("$java -jar $path")
            pc = ProcessSender(process)
        }

        pc?.writeMessage(state.toString())
    }

    fun dispose() {
        pc?.let {
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
                    bind(model.nValueProp.stringBinding { "n = $it" })
                }
                label("min") {
                    bind(model.minValueProp.stringBinding { "min = $it" })
                }
                label("max") {
                    bind(model.maxValueProp.stringBinding { "max = $it" })
                }
            }

            center = vbox {
                button("RUN").action {
                    ctrl.runJar(this@MainView.currentWindow)
                }
            }
        }
    }

    override fun onDelete() {
        ctrl.dispose()
        super.onDelete()
    }
}