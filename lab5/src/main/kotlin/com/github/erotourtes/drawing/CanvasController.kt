package com.github.erotourtes.drawing

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import tornadofx.*

class CanvasController(private val canvas: Canvas) {
    fun getSnapshotImage(): WritableImage =
        try {
            WritableImage(canvas.width.toInt(), canvas.height.toInt()).let { image ->
                canvas.snapshot(null, image)
                image
            }
        } catch (e: Exception) {
            WritableImage(1, 1)
        }

    val fillColorProp = SimpleObjectProperty(Color.BLACK)
    val strokeColorProp = SimpleObjectProperty(Color.BLACK)

    fun changeFillColor(color: Color) {
        canvas.graphicsContext2D.fill = color
        fillColorProp.value = color
        gcUpdate.value = updater.not()
    }

    fun changeStrokeColor(color: Color) {
        canvas.graphicsContext2D.stroke = color
        strokeColorProp.value = color
        gcUpdate.value = updater.not()
    }

    fun changeStrokeWidth(width: Double) {
        canvas.graphicsContext2D.lineWidth = width
        gcUpdate.value = updater.not()
    }

    val gcUpdate = SimpleBooleanProperty(false)
    private val updater by gcUpdate
}