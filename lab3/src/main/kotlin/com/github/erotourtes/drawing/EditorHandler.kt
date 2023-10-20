package com.github.erotourtes.drawing

import com.github.erotourtes.utils.n
import com.github.erotourtes.drawing.editor.Editor
import com.github.erotourtes.drawing.editor.ShapesList
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext

class EditorHandler(private val canvas: Canvas) {
    private val shapes = ShapesList(n)
    private var map: MutableMap<Class<out Editor>, Editor> = mutableMapOf()
    private val curEditor = SimpleObjectProperty<Editor>()

    fun useEditor(editorClass: Class<out Editor>) {
        val editor = getOrCreateEditor(editorClass)
        editor.listenToEvents()
        curEditor.set(editor)
    }

    fun listenToChanges(subscriber: ChangeListener<Editor>) = curEditor.addListener(subscriber)

    private fun getOrCreateEditor(editorClass: Class<out Editor>): Editor = map.getOrPut(editorClass) {
        editorClass.getConstructor(ShapesList::class.java, GraphicsContext::class.java)
            .newInstance(shapes, canvas.graphicsContext2D)
    }
}