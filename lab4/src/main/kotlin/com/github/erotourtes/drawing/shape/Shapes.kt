package com.github.erotourtes.drawing.shape

import com.github.erotourtes.utils.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color


class Point(gc: GraphicsContext) : Shape(gc) {
    override fun draw() {
        val radius = 12.0
        gc.apply {
            val (x, y) = dm.getBoundaries().first
            fillOval(x, y, radius, radius)
        }
    }
}

class Line(gc: GraphicsContext) : Shape(gc) {
    override fun draw() {
        gc.apply { strokeLine(dm) }
    }
}

class Rect(gc: GraphicsContext) : Shape(gc) {
    init {
        colorFill = Color.TRANSPARENT
        colorStroke = Color.BLACK
    }

    override fun draw() {
        gc.apply {
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
        gc.apply {
            fillOval(dm)
            strokeOval(dm)
        }
    }
}

class Dumbbell(gc: GraphicsContext) : Shape(gc) {
    // TODO: make it work as with alt in Photoshop
    override fun draw() {
        gc.apply {
            val radius = 24.0
            val (start, end) = dm.getRaw()

            fillOval(start.x - radius / 2, start.y - radius / 2, radius, radius)
            fillOval(end.x - radius / 2, end.y - radius / 2, radius, radius)

            strokeOval(start.x - radius / 2, start.y - radius / 2, radius, radius)
            strokeOval(end.x - radius / 2, end.y - radius / 2, radius, radius)

            strokeLine(dm)
        }
    }
}