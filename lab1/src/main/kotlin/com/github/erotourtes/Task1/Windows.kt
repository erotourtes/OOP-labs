package com.github.erotourtes.Task1

import javafx.stage.StageStyle
import tornadofx.Fragment
import tornadofx.action
import tornadofx.button
import tornadofx.hbox

class Window1 : Fragment("Window 1") {
    override val root = hbox {
        button("Next") {
            action {
                find<Window2>().openModal(stageStyle = StageStyle.UTILITY)
                close()
            }
        }
        button("Cancel") { action(::close) }
    }
}

class Window2 : Fragment("Window 2") {
    override val root = hbox {
        button("Previous") {
            action {
                find<Window1>().openModal(stageStyle = StageStyle.UTILITY)
                close()
            }
        }
        button("Yes") { action(::close) }
        button("Cancel") { action(::close) }
    }
}