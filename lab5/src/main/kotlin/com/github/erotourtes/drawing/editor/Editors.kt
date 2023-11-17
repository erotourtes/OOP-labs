package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.history.AddItemCommand
import com.github.erotourtes.utils.SingletonHolder
import javafx.scene.input.MouseEvent

class PointEditor private constructor() : Editor() {
    override fun onMousePressed(e: MouseEvent) {
        shape.setDm(dm.setEnd(e.x, e.y))
        redraw()
        previewLine()
    }

    override fun onMouseDragged(e: MouseEvent) {
        onMousePressed(e)
    }

    override fun onMouseReleased(e: MouseEvent) {
        shape.setGCStateWith(gc)
        val command = AddItemCommand(shapes, shape).also { it.execute() }
        history.add(command)

        redraw()
    }

    companion object : SingletonHolder<PointEditor>({ PointEditor() })
}

object EmptyEditor : Editor() {
    override fun listenToEvents() {
        disableEvents()
    }
}

object ShapeEditor : Editor()