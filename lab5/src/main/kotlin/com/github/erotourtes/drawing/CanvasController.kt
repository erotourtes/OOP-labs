package com.github.erotourtes.drawing

import javafx.scene.canvas.Canvas
import javafx.scene.image.WritableImage

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
}