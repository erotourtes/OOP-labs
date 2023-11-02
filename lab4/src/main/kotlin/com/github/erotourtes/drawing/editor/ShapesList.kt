package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.shape.Shape

class ShapesList(n: Int) : Iterable<Shape> {
    private val shapeArr = Array<Shape?>(n) { null }
    private var shapeIndex = 0

    val size: Int
        get() = shapeIndex

    fun add(sh: Shape) {
        if (shapeIndex == shapeArr.size) throw IllegalArgumentException("History is overflow")
        shapeArr[shapeIndex++] = sh
    }

    fun clear() {
        shapeIndex = 0
    }

    fun getList(): List<Shape> = shapeArr.copyOfRange(0, shapeIndex).filterNotNull()

    override fun iterator(): Iterator<Shape> = ShapeIterator()

    inner class ShapeIterator : Iterator<Shape> {
        private var curIndex = 0
        override fun hasNext(): Boolean = curIndex < size
        override fun next(): Shape = if (hasNext()) shapeArr[curIndex++]!! else throw IllegalAccessError()
    }
    override fun toString(): String = "ShapesList(index=$shapeIndex)"
}