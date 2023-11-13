package com.github.erotourtes.drawing.history

import com.github.erotourtes.drawing.editor.ShapesList
import com.github.erotourtes.drawing.shape.Shape

class OpenCommand(private val old: ShapesList, private val new: List<Shape>) : Command {
    private val oldList = old.getList().toList()
    override fun execute() {
        old.clear()
        old.addAll(new)
    }

    override fun undo() {
        old.clear()
        old.addAll(oldList)
    }
}

class NewCommand(private val sl: ShapesList) : Command {
    private val copyList = sl.getList().toList()
    override fun execute() {
        sl.clear()
    }

    override fun undo() {
        sl.clear()
        sl.addAll(copyList)
    }
}

class AddItemCommand(private val shapes: ShapesList, shape: Shape) : Command {
    val shape = shape.copy()
    override fun execute() = shapes.add(shape)
    override fun undo() = shapes.remove(shape)
}