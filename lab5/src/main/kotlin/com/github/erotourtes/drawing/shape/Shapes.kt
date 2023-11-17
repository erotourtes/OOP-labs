package com.github.erotourtes.drawing.shape

import com.github.erotourtes.utils.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlin.math.abs

class Point : Shape() {
    override fun draw(gc: GraphicsContext, dm: Dimension) {
        val radius = 12.0
        gc.apply {
            val (x, y) = dm.getRaw().second
            fillOval(x, y, radius, radius)
            strokeOval(x, y, radius, radius)
        }
    }

    override fun getBounds(dm: Dimension): Dimension {
        val radius = 12.0
        val (x, y) = dm.getRaw().second
        return Dimension.from(x, y, x + radius, y + radius)
    }
}

class Line : Shape() {
    override fun draw(gc: GraphicsContext, dm: Dimension) {
        gc.apply { strokeLine(dm) }
    }
}


class Rect : Shape() {
    override fun draw(gc: GraphicsContext, dm: Dimension) {
        gc.apply {
            fillRect(dm)
            strokeRect(dm)
        }
    }
}

class Ellipse : Shape() {
    override fun draw(gc: GraphicsContext, dm: Dimension) {
        gc.apply {
            fillOval(dm)
            strokeOval(dm)
        }
    }
}

class Dumbbell : Shape() {
    private val line = Line()
    private val ellipse = Ellipse()

    override fun draw(gc: GraphicsContext, dm: Dimension) {
        gc.apply {
            val radius = 24.0
            val hr = radius / 2
            val (start, end) = dm.getRaw()

            line.draw(gc, dm)

            with(ellipse) {
                val d = Dimension

                draw(gc, d.from(start.x - hr, start.y - hr, start.x + hr, start.y + hr))
                draw(gc, d.from(end.x - hr, end.y - hr, end.x + hr, end.y + hr))
            }
        }
    }
}

class CubeEx : Shape() {
    // failed math but got a nice effect
    override fun draw(gc: GraphicsContext, dm: Dimension) {
        gc.apply {
            val (s, bg, size) = getCoords(dm)
            val (bgX, bgY) = bg

            strokeRect(s.x, s.y, size, size)
            strokeRect(bgX, bgY, size, size)

            strokeLine(s.x, s.y, bgX, bgY)
            strokeLine(s.x, s.y + size, bgX, bgY + size)
            strokeLine(s.x + size, s.y, bgX + size, bgY)
            strokeLine(s.x + size, s.y + size, bgX + size, bgY + size)
        }
    }

    override fun drawWithState(gc: GraphicsContext) {
        gc.drawOnce {
            state.gcState.apply(this)
            gc.fill = Color.TRANSPARENT
            draw(gc, state.dm)
        }
    }

    override fun getBounds(dm: Dimension): Dimension {
        val (s, bg, size) = getCoords(dm)
        val (bgX, bgY) = bg

        val d = Dimension
        return d.from(s.x, s.y, bgX + size, bgY + size)
    }

    private fun getCoords(dm: Dimension): Triple<Dimension.Point, Pair<Double, Double>, Double> {
        val (s, e) = dm.getRaw()
        val w = e.x - s.x
        val h = e.y - s.y

        val depthFactor = 0.5
        val size = abs(w.coerceAtLeast(h))
        val depthX = w * depthFactor
        val depthY = h * depthFactor

        val bgX = s.x + depthX
        val bgY = s.y - depthY

        return Triple(s, Pair(bgX, bgY), size)
    }
}

class Cube : Shape() {
    private val square = Rect()
    private val line = Line()

    override fun draw(gc: GraphicsContext, dm: Dimension) {
        gc.apply {
            val (s, bg, size) = getCoords(dm)
            val (bgX, bgY) = bg
            val (sizeX, sizeY) = size

            val d = Dimension

            with(square) {
                draw(gc, d.from(s.x, s.y, s.x + sizeX, s.y + sizeY))
                draw(gc, d.from(bgX, bgY, bgX + sizeX, bgY + sizeY))
            }

            with(line) {
                draw(gc, d.from(s.x, s.y, bgX, bgY))
                draw(gc, d.from(s.x + sizeX, s.y, bgX + sizeX, bgY))
                draw(gc, d.from(s.x, s.y + sizeY, bgX, bgY + sizeY))
                draw(gc, d.from(s.x + sizeX, s.y + sizeY, bgX + sizeX, bgY + sizeY))
            }
        }
    }

    override fun drawWithState(gc: GraphicsContext) {
        gc.drawOnce {
            state.gcState.apply(this)
            gc.fill = Color.TRANSPARENT
            draw(gc, state.dm)
        }
    }

    override fun getBounds(dm: Dimension): Dimension {
        val (s, bg, size) = getCoords(dm)
        val (bgX, bgY) = bg
        val (sizeX, sizeY) = size

        val sX = s.x.coerceAtMost(bgX)
        val sY = s.y.coerceAtMost(bgY)
        val eX = (s.x + sizeX).coerceAtLeast(bgX + sizeX)
        val eY = (s.y + sizeY).coerceAtLeast(bgY + sizeY)

        val d = Dimension
        return d.from(sX, sY, eX, eY)
    }

    private fun getCoords(dm: Dimension): Triple<Dimension.Point, Pair<Double, Double>, Pair<Double, Double>> {
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

        return Triple(s, Pair(bgX, bgY), Pair(sizeX, sizeY))
    }
}

object EmptyShape : Shape() {
    override fun draw(gc: GraphicsContext, dm: Dimension) {}
}
