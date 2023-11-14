package com.github.erotourtes.drawing

import com.github.erotourtes.drawing.editor.Editor
import com.github.erotourtes.drawing.shape.Shape
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import tornadofx.*

class EditorHandler(
    private val canvas: Canvas
) {
    private lateinit var curEditor: Editor
    private var curShape = SimpleObjectProperty<Shape>()

    val shape: Shape by curShape
    val editor: Editor get() = curEditor

    fun use(pair: Pair<Class<out Shape>, Editor>) {
        with(pair) {
            curEditor = second.apply { shape = getShape(first); listenToEvents() }
            curShape.value = curEditor.shape
        }
    }

    fun listenToChanges(subscriber: ChangeListener<Shape>) = curShape.addListener(subscriber)

    fun isCurShapeActive(pair: Pair<Class<out Shape>, Editor>): Boolean = pair.first == curEditor.shape.javaClass

    private fun getShape(clazz: Class<out Shape>) =
        clazz.getConstructor(GraphicsContext::class.java).newInstance(canvas.graphicsContext2D)
}