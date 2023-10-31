package com.github.erotourtes.view

import com.github.erotourtes.drawing.CanvasPane
import javafx.scene.canvas.Canvas
import tornadofx.*

class MainView : View("Lab3") {
    private val canvas = Canvas()
    private val ctrl: MainController by inject(MainController.ScopeInfo(canvas))

    override val root = borderpane {
        top = MenuBar.create(ctrl.editorHandler, ctrl.editorsInfo)
        center = borderpane {
            top = ToolBar.create(ctrl.editorHandler, ctrl.editorsInfo)
            center = CanvasPane(canvas)
        }
    }
}
