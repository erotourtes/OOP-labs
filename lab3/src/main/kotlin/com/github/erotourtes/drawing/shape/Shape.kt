package com.github.erotourtes.drawing.shape

import com.github.erotourtes.utils.Dimension
import com.github.erotourtes.utils.drawOnce
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import java.lang.RuntimeException

abstract class Shape(val gc: GraphicsContext) {
    protected val dm = Dimension()

    var colorFill: Color = Color.BLACK
    var colorStroke: Color = Color.BLACK

    abstract fun draw()

    open fun drawWithProperties() {
        gc.drawOnce {
            setProperties()
            draw()
        }
    }

    open fun setDm(curDm: Dimension) = curDm.copyTo(dm)

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
}
