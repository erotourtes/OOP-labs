package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.shape.*
import javafx.scene.paint.Color
import com.github.erotourtes.utils.*
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent

abstract class Editor(protected val shapes: ShapesList, protected val gc: GraphicsContext) {
    protected val dm = Dimension()

    protected open var curProcessor: DmProcessor = DmProcessor { it }
    protected open val processor: DmProcessor = DmProcessor { it }
    protected open val altProcessor: DmProcessor = DmProcessor { Dimension.toCorner(it) }
    protected open val ctrlProcessor: DmProcessor = DmProcessor { Dimension.toEqual(it) }
    protected abstract val shape: Shape

    private var isStillDrawing = false

    open fun listenToEvents() {
        val c = gc.canvas
        c.isFocusTraversable = true
        c.requestFocus()
        c.setOnMousePressed(::onMousePressed)
        c.setOnMouseDragged(::onMouseDragged)
        c.setOnMouseReleased(::onMouseReleased)
        c.setOnKeyPressed(::onKeyPressed)
        c.setOnKeyReleased(::onKeyReleased)
    }

    open fun disableEvents() {
        with(gc.canvas) {
            onMousePressed = null
            onMouseDragged = null
            onMouseReleased = null
            onKeyPressed = null
            onKeyReleased = null
        }
    }

    protected open fun onMousePressed(e: MouseEvent) {
        redraw()
        dm.setStart(e.x, e.y)
        isStillDrawing = true
    }

    protected open fun onMouseDragged(e: MouseEvent) {
        redraw()

        dm.setEnd(e.x, e.y)
        previewLine()
    }

    protected open fun onMouseReleased(e: MouseEvent) {
        isStillDrawing = false
        if (e.isDragDetect) return // returns if mouse was not dragged
        shape.setDm(curProcessor.process(dm))
        shapes.add(shape.copy())
        redraw()
    }

    protected open fun onKeyPressed(e: KeyEvent) {
        changeProcessor(e)

        if (!isStillDrawing) return
        redraw()
        previewLine()
    }

    protected open fun onKeyReleased(e: KeyEvent) = onKeyPressed(e)

    protected open fun changeProcessor(e: KeyEvent) {
        curProcessor = processor
        if (e.isControlDown) curProcessor = pipe(curProcessor, ctrlProcessor)
        if (e.isAltDown) curProcessor = pipe(curProcessor, altProcessor)
    }


    private fun drawAll() {
        for (shape in shapes) shape.drawWithProperties()
    }

    private fun clear() = gc.clearRect(0.0, 0.0, gc.canvas.width, gc.canvas.height)

    fun redraw() {
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
