package com.github.erotourtes.drawing.history

import com.github.erotourtes.drawing.editor.ShapesList
import com.github.erotourtes.drawing.shape.Shape
import com.github.erotourtes.utils.Dimension

/**
 *  Command that is used to change the whole list of shapes.
 */
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

/**
 *  Command that is used to erase the whole list of shapes.
 */
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

/**
 *  Command that is used to add a shape to the list of shapes.
 */
class AddItemCommand(private val shapes: ShapesList, shape: Shape) : Command {
    val shape = shape.copy()
    override fun execute() = shapes.add(shape)
    override fun undo() = shapes.remove(shape)
}

/**
 *  Command that is used to remove a shape from the list of shapes.
 */
class RemoveItemCommand(private val shapes: ShapesList, private val shape: Shape) : Command {
    override fun execute() = shapes.remove(shape)
    override fun undo() = shapes.add(shape)
}

/**
 *  Command that is used to change the coordinates of a shape.
 */
class ChangeCoordinatesCommand(shapeModel: Shape.ShapeModel, private val redraw: () -> Unit) : Command {
    private val shape = shapeModel.item
    private val dmNew =
        Dimension.from(shapeModel.x1.value, shapeModel.y1.value, shapeModel.x2.value, shapeModel.y2.value)
    private val dmOld = shapeModel.item.copyDm


    override fun execute() {
        shape.setDm(dmNew)
        redraw()
    }

    override fun undo() {
        shape.setDm(dmOld)
        redraw()
    }
}