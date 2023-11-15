package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.history.AddItemCommand
import com.github.erotourtes.utils.SingletonHolder
import javafx.scene.input.MouseEvent

class PointEditor private constructor() : Editor() {
    override fun onMouseDragged(e: MouseEvent) {}

    override fun onMousePressed(e: MouseEvent) {
        dm.setEnd(e.x, e.y)
        super.onMousePressed(e)
        shape.setDm(dm)
    }

    override fun onMouseReleased(e: MouseEvent) {
        val command = AddItemCommand(shapes, shape).also { it.execute() }
        history.add(command)

        redraw()
    }

    override fun previewLine() {}

    companion object : SingletonHolder<PointEditor>({ PointEditor() })
}

object EmptyEditor : Editor() {
    override fun listenToEvents() {
        disableEvents()
    }
}

object ShapeEditor : Editor()