package com.github.erotourtes.drawing

import com.github.erotourtes.drawing.shape.EmptyShape
import com.github.erotourtes.drawing.shape.Shape
import com.github.erotourtes.drawing.shape.ShapeState
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.image.WritableImage

class CanvasController(private val canvas: Canvas) {
    fun getSnapshotImage(): WritableImage = WritableImage(canvas.width.toInt(), canvas.height.toInt()).let { image ->
        canvas.snapshot(null, image)
        image
    }

    fun initShape(shapeState: ShapeState) = shapeState.toShape(canvas.graphicsContext2D)

    fun initShape(clazz: Class<out Shape>) = clazz.getConstructor(GraphicsContext::class.java).newInstance(canvas.graphicsContext2D)

    fun emptyShape() = initShape(EmptyShape::class.java)
}