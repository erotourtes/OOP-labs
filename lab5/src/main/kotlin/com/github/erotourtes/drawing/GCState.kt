package com.github.erotourtes.drawing

import com.github.erotourtes.utils.GCStateSerializer
import javafx.scene.canvas.GraphicsContext
import javafx.scene.paint.Color
import kotlinx.serialization.Serializable

@Serializable
data class GCState(
    val fill: Color,
    val stroke: Color,
    val lineWidth: Double,
) {
    fun apply(gc: GraphicsContext) {
        val s = this
        gc.apply {
            fill = s.fill
            stroke = s.stroke
            lineWidth = s.lineWidth
        }
    }

    fun copy() = GCState(fill, stroke, lineWidth)

    companion object {
        fun from(gc: GraphicsContext) = GCState(
            gc.fill as Color,
            gc.stroke as Color,
            gc.lineWidth,
        )

        val default
            get() = GCState(
                Color.BLACK,
                Color.BLACK,
                1.0,
            )

        val serializer get() = GCStateSerializer
    }
}