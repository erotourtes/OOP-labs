package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.shape.Shape
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener

class ShapesList : Iterable<Shape> {
    private val shapeArr = FXCollections.observableArrayList<Shape>()

    val size: Int
        get() = shapeArr.size

    fun add(sh: Shape) {
        shapeArr.add(sh)
    }

    fun addAll(shapes: List<Shape>) {
        shapeArr.addAll(shapes)
    }

    fun addListener(listener: ListChangeListener<Shape>) = shapeArr.addListener(listener)

    fun removeListener(listener: ListChangeListener<Shape>) = shapeArr.removeListener(listener)

    fun remove(sh: Shape) {
        shapeArr.remove(sh)
    }

    fun clear() {
        shapeArr.clear()
    }

    fun getList(): List<Shape> = shapeArr.toList()

    override fun iterator(): Iterator<Shape> = shapeArr.iterator()
    override fun toString(): String = "ShapesList(${shapeArr.size})"
}