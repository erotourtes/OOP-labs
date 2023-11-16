package com.github.erotourtes.drawing.shape

import com.github.erotourtes.utils.Dimension
import com.github.erotourtes.utils.drawOnce
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import tornadofx.*
import java.lang.RuntimeException

abstract class Shape(val gc: GraphicsContext) {
    protected val dm = Dimension()

    var colorFill: Color = Color.BLACK
    var colorStroke: Color = Color.BLACK

    abstract fun draw()

    open fun draw(dm: Dimension) = setDm(dm).draw()

    fun drawOnce(dm: Dimension, setDefaultProps: Boolean = false) {
        val copy = dmCopy
        setDm(dm)
        if (setDefaultProps) drawWithProperties()
        else draw()
        setDm(copy)
    }

    open fun drawWithProperties() {
        gc.drawOnce {
            setProperties()
            draw()
        }
    }

    open fun setDm(curDm: Dimension) = curDm.copyTo(dm).let { this }

    val dmCopy get() = dm.copy()

    private fun setProperties() {
        with(gc) {
            fill = colorFill
            stroke = colorStroke
        }
    }

    fun copy(): Shape {
        try {
            val shape = this::class.java.getConstructor(GraphicsContext::class.java).newInstance(gc)
            shape.dm.copyFrom(dm)
            shape.colorFill = colorFill
            shape.colorStroke = colorStroke
            return shape
        } catch (e: Exception) {
            throw RuntimeException("Can't copy a shape")
        }
    }

    fun getState(): ShapeState = ShapeState(
        this::class.java.name,
        dm.copy(),
        colorFill,
        colorStroke,
    )

    val model = ShapeModel(this)

    class ShapeModel(shape: Shape) : ItemViewModel<Shape>(shape) {

        override fun onCommit() {
            super.onCommit()
            item.dm.model.commit()
        }

        val x1 = bind { item.dm.model.x1 }
        val x2 = bind { item.dm.model.x2 }
        val y1 = bind { item.dm.model.y1 }
        val y2 = bind { item.dm.model.y2 }
    }
}
