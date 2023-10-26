package com.github.erotourtes.view

import com.github.erotourtes.drawing.CanvasPane
import com.github.erotourtes.drawing.EditorHandler
import javafx.scene.canvas.Canvas
import tornadofx.*

class MainView : View("Lab3") {
    private val canvas = Canvas()
    private val editorHandler = EditorHandler(canvas)

    override val root = borderpane {
        top = MenuBar.create(editorHandler)
        center = borderpane {
            top = ToolBar.create(editorHandler)
            center = CanvasPane(canvas)
        }
    }
}
