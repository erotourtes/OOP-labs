package com.github.erotourtes.task2

import javafx.beans.property.SimpleStringProperty
import javafx.stage.StageStyle
import tornadofx.*

class TaskView : View("Task 2") {
    private val message = SimpleStringProperty("")

    override val root = vbox {
        label { text = "Doing task 2, @3" }

        button("Start") {
            action {
                find<Window>(mapOf(Window::message to message)).openModal(stageStyle = StageStyle.UTILITY)
            }
        }

        label(message)
    }
}
