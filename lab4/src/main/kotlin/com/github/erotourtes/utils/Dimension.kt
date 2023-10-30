package com.github.erotourtes.utils

import kotlin.math.abs

class Dimension {
    private var x1: Double = 0.0
    private var y1: Double = 0.0
    private var x2: Double = 0.0
    private var y2: Double = 0.0

    val width: Double
        get() = abs(x2 - x1)
    val height: Double
        get() = abs(y2 - y1)

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

    fun getBoundaries(): Pair<Point, Point> {
        return Pair(
            Point(x1.coerceAtMost(x2), y1.coerceAtMost(y2)),
            Point(x1.coerceAtLeast(x2), y1.coerceAtLeast(y2))
        )
    }

    fun getRaw(): Pair<Point, Point> {
        return Pair(
            Point(x1, y1),
            Point(x2, y2)
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
    }
}
