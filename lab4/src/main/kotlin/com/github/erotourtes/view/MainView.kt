package com.github.erotourtes.view

import javafx.scene.input.KeyCode
import tornadofx.*

class MainView : View("Lab4") {
    override val scope = MyScope()
    private val ctrl: MainController by inject(scope)

    override val root = borderpane {
        ctrl.populate()
//  val model = scope.canvasModel // has no effect, because it's not the same scope
//  val model = find<CanvasModel>(scope)

        top = find<MyMenu>(scope).root
        center = borderpane {
            top = find<ToolBar>(scope).root
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
