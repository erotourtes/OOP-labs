package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.shape.*
import javafx.scene.paint.Color
import com.github.erotourtes.utils.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseEvent

abstract class Editor(protected val shapes: ShapesList, protected val gc: GraphicsContext) {
    protected val dm = Dimension()

    protected open var curProcessor: DmProcessor = DmProcessor { it }
    protected open val processor: DmProcessor = DmProcessor { it }
    protected open val altProcessor: DmProcessor = DmProcessor { Dimension.toCorner(it) }
    protected open val ctrlProcessor: DmProcessor = DmProcessor { Dimension.toEqual(it) }
    protected abstract val shape: Shape

    open fun listenToEvents() {
        val c = gc.canvas
        c.setOnMousePressed(::onMousePressed)
        c.setOnMouseDragged(::onMouseDragged)
        c.setOnMouseReleased(::onMouseReleased)
    }

    protected open fun onMousePressed(e: MouseEvent) {
        redraw()
        dm.setStart(e.x, e.y)
    }

    protected open fun onMouseDragged(e: MouseEvent) {
        redraw()

        if (e.isAltDown) curProcessor = altProcessor
        else if (e.isControlDown) curProcessor = ctrlProcessor
        else curProcessor = processor

        dm.setEnd(e.x, e.y)
        previewLine()
    }

    protected open fun onMouseReleased(e: MouseEvent) {
        if (e.isDragDetect) return // returns if mouse was not dragged
        shape.setDm(curProcessor.process(dm))
        shapes.add(shape.copy())
        redraw()
    }

    private fun drawAll() {
        for (shape in shapes) shape.drawWithProperties()
    }

    private fun clear() = gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height)

    protected fun redraw() {
        clear()
        drawAll()
    }

    protected open fun previewLine() {
        gc.drawOnce {
            setPreviewProperties()
            shape.setDm(curProcessor.process(dm))
            shape.draw()
        }
    }

    protected fun setPreviewProperties() {
        gc.setLineDashes(5.0)
        gc.stroke = Color.BLACK
        gc.fill = Color.TRANSPARENT
    }
}
