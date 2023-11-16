package com.github.erotourtes.drawing.shape

import com.github.erotourtes.drawing.GCState
import com.github.erotourtes.utils.Dimension
import com.github.erotourtes.utils.drawOnce
import javafx.scene.canvas.GraphicsContext

abstract class Shape {
    protected var state = ShapeState(
        this::class.java.name,
        Dimension(),
        GCState.default,
    )

    /**
     * Draw the shape without saving the dimension with the default GraphicsContext properties
     */
    abstract fun draw(gc: GraphicsContext, dm: Dimension)

    open fun drawWithState(gc: GraphicsContext) {
        gc.drawOnce {
            state.gcState.apply(this)
            draw(gc, state.dm)
        }
    }

    fun setDm(dm: Dimension) = dm.copyTo(state.dm).let { this }

    open fun copy(): Shape {
        try {
            val shape = this::class.java.getConstructor().newInstance()
            shape.state = state.copy()
            return shape
        } catch (e: Exception) {
            throw RuntimeException("Can't copy a shape")
        }
    }

    val copyDm get() = state.dm.copy()
    val copyState get() = state.copy()

    fun setStateWith(state: ShapeState) {
        this.state = state
    }
}
