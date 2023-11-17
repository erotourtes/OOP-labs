package com.github.erotourtes.drawing

import com.github.erotourtes.drawing.editor.Editor
import com.github.erotourtes.drawing.editor.EmptyEditor
import com.github.erotourtes.drawing.shape.Shape
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.value.ChangeListener
import tornadofx.*

class EditorHandler(
    private val shapeMap: Map<Class<out Shape>, Editor>
) {
    private var curEditor: Editor = EmptyEditor
        set(value) {
            field.disableEvents()
            field = value
            value.listenToEvents()
        }

    private var curShape = SimpleObjectProperty<Shape>()

    val shape: Shape by curShape
    val editor: Editor get() = curEditor

    fun use(pair: Pair<Class<out Shape>, Editor>) {
        with(pair) {
            curEditor = second.apply { shape = getShape(first) }
            curShape.value = curEditor.shape
        }
    }

    fun requestRedraw() = curEditor.redraw()

    fun listenToChanges(subscriber: ChangeListener<Shape>) = curShape.addListener(subscriber)

    fun isCurShapeActive(pair: Pair<Class<out Shape>, Editor>): Boolean = pair.first == curEditor.shape.javaClass

    private fun getShape(clazz: Class<out Shape>) =
        if (clazz.constructors.isEmpty()) clazz.kotlin.objectInstance!!
        else clazz.getConstructor().newInstance()
}