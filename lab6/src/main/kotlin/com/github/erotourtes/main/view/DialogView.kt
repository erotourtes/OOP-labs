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
                        bind(model.nProp)
                        filterInput { it.controlNewText.isInt() }
                    }
                }
                field("Input min value") {
                    textfield {
                        bind(model.minProp)
                        filterInput { it.controlNewText.isDouble() }
                    }
                }
                field("Input max value") {
                    textfield {
                        bind(model.maxProp)
                        filterInput { it.controlNewText.isDouble() }
                    }
                }
            }
        }

        button("Apply").action {
            with(model) {
                if (min > max) minProp.value = max.apply { maxProp.value = min }

                commit()
            }
            close()
        }
    }

    init {
        primaryStage.isAlwaysOnTop = true
    }
}