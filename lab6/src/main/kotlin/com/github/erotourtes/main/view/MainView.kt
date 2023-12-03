package com.github.erotourtes.main.view

import com.github.erotourtes.data.MainModel
import com.github.erotourtes.data.MainState
import javafx.stage.FileChooser
import javafx.stage.StageStyle
import javafx.stage.Window
import tornadofx.*

class MainController : Controller() {
    private val dialog: DialogView by inject()
    private val model: MainModel by inject()
    private var state = MainState()

    init {
        model.item = state
    }

    fun showDialog() {
        dialog.openModal(stageStyle = StageStyle.UTILITY)
    }

    fun runJar(window: Window?) {
        val file = FileChooser().apply {
            title = "Select jar file"
            extensionFilters.add(FileChooser.ExtensionFilter("Jar files", "*.jar"))
        }.showOpenDialog(window) ?: return

        val java = "${System.getProperty("java.home")}/bin/java"
        val path = file.absolutePath

        val process = Runtime.getRuntime().exec("$java -jar $path")
        process.waitFor()
    }
}

class DialogView : View("Set Properties") {
    private val model by inject<MainModel>()

    override val root = vbox {
        label("Dialog")

        form {
            fieldset {
                field("Input n value") {
                    textfield {
                        bind(model.nValueProp)
                        filterInput { it.controlNewText.isInt() }
                    }
                }
                field("Input min value") {
                    textfield {
                        bind(model.minValueProp)
                        filterInput { it.controlNewText.isInt() }
                    }
                }
                field("Input max value") {
                    textfield {
                        bind(model.maxValueProp)
                        filterInput { it.controlNewText.isInt() }
                    }
                }
            }
        }

        button("Close").action {
            model.commit()
            close()
        }
    }

    init {
        primaryStage.isAlwaysOnTop = true
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
}

