package com.github.erotourtes.drawing.shape

import com.github.erotourtes.utils.Dimension
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color

abstract class Shape(val gc: GraphicsContext) {
    protected val dm = Dimension()

    var colorFill: Color = Color.BLACK
    var colorStroke: Color = Color.BLACK

    abstract fun draw()

    open fun setDm(curDm: Dimension) = curDm.copyTo(dm)

    fun setProperties() {
        with(gc) {
            fill = colorFill
            stroke = colorStroke
        }
    }

    fun copy(): Shape {
        val shape = this::class.java.getConstructor(GraphicsContext::class.java).newInstance(gc)
        shape.dm.copyFrom(dm)
        shape.colorFill = colorFill
        shape.colorStroke = colorStroke
        return shape
    }
}
