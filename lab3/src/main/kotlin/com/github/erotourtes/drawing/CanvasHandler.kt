package com.github.erotourtes.drawing

import com.github.erotourtes.view.n
import com.github.erotourtes.drawing.editor.Editor
import com.github.erotourtes.drawing.editor.ShapesList
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext

class CanvasHandler {
    val canvas = Canvas()
    private val shapes = ShapesList(n)
    private var map: MutableMap<Class<out Editor>, Editor> = mutableMapOf()

    inline fun <reified T : Editor> useEditor() {
        val editorClass = T::class.java as Class<out Editor>
        val editor = getOrCreateEditor(editorClass)
        editor.listenToEvents()
    }

    fun getOrCreateEditor(editorClass: Class<out Editor>): Editor = map.getOrPut(editorClass) {
        editorClass.getConstructor(ShapesList::class.java, GraphicsContext::class.java)
            .newInstance(shapes, canvas.graphicsContext2D)
    }
}