package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.shape.*
import javafx.scene.paint.Color
import com.github.erotourtes.utils.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent

abstract class Editor(protected val shapes: ShapesList, protected val gc: GraphicsContext) {
    protected val dm = Dimension()
    protected abstract val shape: Shape

    open fun listenToEvents() {
        val c = gc.canvas
        c.setOnMousePressed(this::onMousePressed)
        c.setOnMouseDragged(this::onMouseDragged)
        c.setOnMouseReleased(this::onMouseReleased)
    }

    protected open fun onMousePressed(e: MouseEvent) {
        redraw()
        dm.setStart(e.x, e.y)
    }

    protected open fun onMouseDragged(e: MouseEvent) {
        redraw()

        dm.setEnd(e.x, e.y)
        previewLine()
    }

    protected open fun onMouseReleased(e: MouseEvent) {
        if (e.isDragDetect) return // returns if mouse was not dragged
        shape.setDm(dm)
        shapes.add(shape.copy())
        redraw()
    }

    private fun drawAll() {
        for (shape in shapes) shape.draw()
    }

    private fun clear() = gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height)

    protected fun redraw() {
        clear()
        drawAll()
    }

    abstract fun previewLine()

    protected fun setPreviewProperties() {
        gc.setLineDashes(5.0)
        gc.stroke = Color.BLACK
    }
}
