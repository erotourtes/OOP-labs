package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.shape.*
import javafx.scene.canvas.Canvas
import javafx.scene.paint.Color
import com.github.erotourtes.utils.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent

abstract class Editor(private val canvas: Canvas, private val shapes: ShapesList) {
    protected val dm = Dimension()
    protected val gc: GraphicsContext = canvas.graphicsContext2D

    protected abstract val shape: Shape

    open fun listenToEvents() {
        canvas.setOnMousePressed(this::onMousePressed)
        canvas.setOnMouseDragged(this::onMouseDragged)
        canvas.setOnMouseReleased(this::onMouseReleased)
    }

    protected open fun onMousePressed(e: MouseEvent) {
        clear()
        drawAll()
        dm.setStart(e.x, e.y)
    }

    protected open fun onMouseDragged(e: MouseEvent) {
        clear()
        drawAll()

        dm.setEnd(e.x, e.y)

        drawShowLine()
        shape.draw(dm)
    }

    protected open fun onMouseReleased(e: MouseEvent) {
        shapes.add(shape.copy())
    }

    private fun drawAll() {
        for (shape in shapes) shape.draw()
    }

    private fun clear() {
        gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height)
    }

    protected open fun drawShowLine() {
        gc.drawOnce {
            gc.stroke = Color.BLUE
            gc.strokeRect(dm)
        }
    }
}