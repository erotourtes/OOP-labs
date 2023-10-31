package com.github.erotourtes.drawing.shape

import com.github.erotourtes.utils.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.abs

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
    private val line = Line(gc)
    private val ellipse = Ellipse(gc)

    override fun draw() {
        gc.apply {
            val radius = 24.0
            val hr = radius / 2
            val (start, end) = dm.getRaw()

            with(ellipse) {
                val d = Dimension
                draw(d.from(start.x - hr, start.y - hr, start.x + hr, start.y + hr))
                draw(d.from(end.x - hr, end.y - hr, end.x + hr, end.y + hr))
            }

            line.draw(dm)
        }
    }
}

class CubeEx(gc: GraphicsContext) : Shape(gc) {
    // failed math but got a nice effect
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

class Cube(gc: GraphicsContext) : Shape(gc) {
    private val square = Rect(gc)
    private val line = Line(gc)

    init {
        colorFill = Color.TRANSPARENT
    }

    override fun draw() {
        gc.apply {
            var (s, e) = dm.getRaw()
            val w = e.x - s.x
            val h = e.y - s.y

            val depthFactor = 0.5
            val sizeX = abs(w)
            val sizeY = abs(h)
            val depthX = w * depthFactor
            val depthY = h * depthFactor

            s = dm.getBoundaries().first

            val bgX = s.x + depthX
            val bgY = s.y - depthY

            val d = Dimension

            with(square) {
                draw(d.from(s.x, s.y, s.x + sizeX, s.y + sizeY))
                draw(d.from(bgX, bgY, bgX + sizeX, bgY + sizeY))
            }

            with(line) {
                draw(d.from(s.x, s.y, bgX, bgY))
                draw(d.from(s.x + sizeX, s.y, bgX + sizeX, bgY))
                draw(d.from(s.x, s.y + sizeY, bgX, bgY + sizeY))
                draw(d.from(s.x + sizeX, s.y + sizeY, bgX + sizeX, bgY + sizeY))
            }
        }
    }
}
