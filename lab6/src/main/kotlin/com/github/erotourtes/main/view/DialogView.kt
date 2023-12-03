package com.github.erotourtes.main.view

import com.github.erotourtes.data.MainModel
import tornadofx.*

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

        button("Apply").action {
            model.commit()
            close()
        }
    }

    init {
        primaryStage.isAlwaysOnTop = true
    }
}