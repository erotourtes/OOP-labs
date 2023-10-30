package com.github.erotourtes.drawing.shape

import com.github.erotourtes.utils.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.abs
import kotlin.math.cos


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

class Cube(gc: GraphicsContext) : Shape(gc) {
    override fun draw() {
        gc.apply {
            val (s, e) = dm.getRaw()
            val w = e.x - s.x
            val h = e.y - s.y

            val depthFactor = 0.5
            val size = abs(w.coerceAtLeast(h))
            val depthX = w * depthFactor
            val depthY = h * depthFactor

            val bgX = s.x + depthX
            val bgY = s.y - depthY

            strokeRect(s.x, s.y, size, size)
            strokeRect(bgX, bgY, size, size)

            strokeLine(s.x, s.y, bgX, bgY)
            strokeLine(s.x, s.y + size, bgX, bgY + size)
            strokeLine(s.x + size, s.y, bgX + size, bgY)
            strokeLine(s.x + size, s.y + size, bgX + size, bgY + size)
        }
    }
}