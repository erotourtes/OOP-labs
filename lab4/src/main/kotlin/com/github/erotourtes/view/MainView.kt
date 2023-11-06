package com.github.erotourtes.view

import javafx.scene.input.KeyCode
import tornadofx.*

class MainView : View("Lab4") {
    private val ctrl by inject<MainController>()

    override val root = borderpane {
        ctrl.populate()

        top = find<MyMenu>().root
        center = borderpane {
            top = find<ToolBar>().root
            center = pane { ctrl.bindCanvas(this) }
        }

        setOnKeyPressed { event ->
            when {
                event.isControlDown && event.code == KeyCode.Z -> ctrl.undo()
                event.isShiftDown && event.code == KeyCode.Z -> ctrl.redo()
            }
        }
    }
}
