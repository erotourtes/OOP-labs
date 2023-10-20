package com.github.erotourtes.drawing.shape

import com.github.erotourtes.utils.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color


class Point(gc: GraphicsContext) : Shape(gc) {
    override fun draw() {
        val radius = 12.0
        gc.drawOnce {
            setProperties()
            val (x, y) = dm.getBoundaries().first
            fillOval(x, y, radius, radius)
        }
    }
}

class Line(gc: GraphicsContext) : Shape(gc) {
    override fun draw() {
        gc.drawOnce {
            setProperties()
            strokeLine(dm)
        }
    }
}

class Rect(gc: GraphicsContext) : Shape(gc) {
    init {
        colorFill = Color.TRANSPARENT
        colorStroke = Color.BLACK
    }

    override fun setDm(curDm: Dimension) = dm.copyFrom(getToCornerDimension(curDm))

    override fun draw() {
        gc.drawOnce {
            setProperties()
            fillRect(dm)
            strokeRect(dm)
        }
    }
}

class Ellipse(gc: GraphicsContext) : Shape(gc) {
    init {
        colorFill = Color.ORANGE
        colorStroke = Color.BLACK
    }

    override fun draw() {
        gc.drawOnce {
            setProperties();
            fillOval(dm)
            strokeOval(dm)
        }
    }

    override fun setDm(curDm: Dimension) = dm.copyFrom(getToCornerDimension(curDm))
}