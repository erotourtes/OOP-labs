package com.github.erotourtes.task1

import javafx.stage.StageStyle
import tornadofx.*

class TaskView : View("Task 1") {
    override val root = vbox {
        label { text = "Doing task 1, @2" }

        button("Start") {
            action {
                find<Window1>().openModal(stageStyle = StageStyle.UTILITY)
            }
        }
    }
}