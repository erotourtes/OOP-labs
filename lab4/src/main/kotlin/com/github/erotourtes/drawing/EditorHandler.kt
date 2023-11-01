package com.github.erotourtes.drawing

import com.github.erotourtes.drawing.editor.Editor
import com.github.erotourtes.drawing.editor.ShapesList
import com.github.erotourtes.utils.EditorFactory
import com.github.erotourtes.utils.n
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.scene.canvas.Canvas

class EditorHandler(private val factories: Map<String, EditorFactory>, private val canvas: Canvas) {
    private val shapes = ShapesList(n)
    private var editors: MutableMap<String, Editor> = mutableMapOf()
    private val curEditor = SimpleObjectProperty<String>()

    fun useEditor(editorName: String) {
        editors[curEditor.get()]?.disableEvents()

        val editor = getOrCreateEditor(editorName)
        editor.listenToEvents()
        curEditor.set(editorName)
    }

    fun listenToChanges(subscriber: ChangeListener<String>) = curEditor.addListener(subscriber)

    private fun getOrCreateEditor(editorName: String): Editor = editors.getOrPut(editorName) {
        factories[editorName]?.create(shapes, canvas.graphicsContext2D)
            ?: throw Exception("Editor with name $editorName is not found")
    }
}