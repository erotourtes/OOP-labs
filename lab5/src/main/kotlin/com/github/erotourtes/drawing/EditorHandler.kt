package com.github.erotourtes.drawing

import com.github.erotourtes.drawing.editor.Editor
import com.github.erotourtes.drawing.shape.Shape
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import javafx.scene.canvas.Canvas
import tornadofx.*
import java.util.NoSuchElementException

class EditorHandler(
    private val canvas: Canvas,
    private val shapeMap: Map<Class<out Shape>, Editor>
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

//    fun use(clazz: Class<out Shape>) {
//        val editor = shapeMap[clazz] ?: throw NoSuchElementException("No editor for $clazz")
//        use((clazz to editor))
//    }

    fun use(shape: Shape) {
        val editor = shapeMap[shape.javaClass] ?: throw NoSuchElementException("No editor for ${shape.javaClass}")
        curEditor = editor.apply { this.shape = shape; listenToEvents() }
        curShape.value = curEditor.shape
    }

    fun listenToChanges(subscriber: ChangeListener<Shape>) = curShape.addListener(subscriber)

    fun isCurShapeActive(pair: Pair<Class<out Shape>, Editor>): Boolean = pair.first == curEditor.shape.javaClass

    private fun getShape(clazz: Class<out Shape>) =
        if (clazz.constructors.isEmpty()) clazz.kotlin.objectInstance!!
        else clazz.getConstructor().newInstance()
}