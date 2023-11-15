package com.github.erotourtes.drawing.editor

import com.github.erotourtes.drawing.history.AddItemCommand
import com.github.erotourtes.drawing.history.History
import com.github.erotourtes.drawing.shape.*
import javafx.scene.paint.Color
import com.github.erotourtes.utils.*
import javafx.collections.ListChangeListener
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent

abstract class Editor {
    protected lateinit var shapes: ShapesList
    protected lateinit var gc: GraphicsContext
    protected lateinit var history: History
    private lateinit var _shape: Shape

    var shape: Shape
        get() = _shape
        set(value) {
            this._shape = value
        }

    fun init(shapes: ShapesList, gc: GraphicsContext, history: History, shape: Shape) {
        this.shapes = shapes
        this.gc = gc
        this.history = history
        this.shape = shape
    }

    protected val dm = Dimension()

    protected open var curProcessor: DmProcessor = DmProcessor { it }
    protected open val processor: DmProcessor = DmProcessor { it }
    protected open val altProcessor: DmProcessor = DmProcessor { Dimension.toCorner(it) }
    protected open val ctrlProcessor: DmProcessor = DmProcessor { Dimension.toEqual(it) }

    protected open val shapesChangeListener = ListChangeListener<Shape> { redraw() }

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
        shapes.addListener(shapesChangeListener)
    }

    open fun disableEvents() {
        with(gc.canvas) {
            onMousePressed = null
            onMouseDragged = null
            onMouseReleased = null
            onKeyPressed = null
            onKeyReleased = null
        }
        shapes.removeListener(shapesChangeListener)
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

        val command = AddItemCommand(shapes, shape).also { it.execute() }
        history.add(command)

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
