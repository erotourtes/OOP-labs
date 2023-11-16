package com.github.erotourtes.utils

import javafx.beans.property.SimpleDoubleProperty
import kotlinx.serialization.Serializable
import tornadofx.*
import kotlin.math.abs

@Serializable
class Dimension {
    private val x1Prop = SimpleDoubleProperty(0.0)
    private val y1Prop = SimpleDoubleProperty(0.0)
    private val x2Prop = SimpleDoubleProperty(0.0)
    private val y2Prop = SimpleDoubleProperty(0.0)

    private var x1 by x1Prop
    private var y1 by y1Prop
    private var x2 by x2Prop
    private var y2 by y2Prop

    val getX1Prop get() = x1Prop
    val getY1Prop get() = y1Prop
    val getX2Prop get() = x2Prop
    val getY2Prop get() = y2Prop

    val width: Double get() = abs(x2 - x1)
    val height: Double get() = abs(y2 - y1)

    fun setStart(x: Double, y: Double): Dimension {
        x1 = x
        y1 = y
        return this
    }

    fun setEnd(x: Double, y: Double): Dimension {
        x2 = x
        y2 = y
        return this
    }

    fun copyTo(dst: Dimension) {
        dst.x1 = x1
        dst.y1 = y1
        dst.x2 = x2
        dst.y2 = y2
    }

    fun copyFrom(src: Dimension) = src.copyTo(this)

    fun copy(): Dimension = Dimension().apply { copyFrom(this@Dimension) }

    fun getBoundaries(): Pair<Point, Point> {
        return Pair(
            Point(x1.coerceAtMost(x2), y1.coerceAtMost(y2)), Point(x1.coerceAtLeast(x2), y1.coerceAtLeast(y2))
        )
    }

    fun getRaw(): Pair<Point, Point> {
        return Pair(
            Point(x1, y1), Point(x2, y2)
        )
    }

    data class Point(val x: Double, val y: Double)

    override fun toString(): String = "Dimension(x1=$x1, y1=$y1, x2=$x2, y2=$y2)"

    companion object {
        fun toCorner(dm: Dimension): Dimension {
            val (c, end) = dm.getRaw()

            // it is not width, it is half of the width; can be negative
            val w = end.x - c.x
            val h = end.y - c.y

            val sX = c.x - w
            val sY = c.y - h

            return Dimension().setStart(end.x, end.y).setEnd(sX, sY)
        }

        fun toEqual(dm: Dimension): Dimension {
            val (s, e) = dm.getRaw()
            val w = e.x - s.x
            val h = e.y - s.y
            val size = abs(w).coerceAtLeast(abs(h))
            val safeW = if (w == 0.0) 1.0 else w
            val safeH = if (h == 0.0) 1.0 else h
            val normalizedX = safeW / abs(safeW) * size
            val normalizedY = safeH / abs(safeH) * size

            return Dimension().setStart(s.x, s.y).setEnd(s.x + normalizedX, s.y + normalizedY)
        }

        fun from(x1: Double, y1: Double, x2: Double, y2: Double): Dimension =
            Dimension().setStart(x1, y1).setEnd(x2, y2)

        val serializer get() = DimensionSerializer
    }
}
