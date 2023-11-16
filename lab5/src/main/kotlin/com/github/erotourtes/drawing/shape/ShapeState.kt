package com.github.erotourtes.drawing.shape

import com.github.erotourtes.drawing.GCState
import com.github.erotourtes.utils.Dimension
import com.github.erotourtes.utils.ShapeStateSerializer
import kotlinx.serialization.Serializable

@Serializable
data class ShapeState(
    val clazz: String,
    val dm: Dimension,
    var gcState: GCState,
) {
    fun copy() = ShapeState(clazz, dm.copy(), gcState.copy())

    fun toShape(): Shape {
        val shape = Class.forName(clazz).getConstructor().newInstance() as Shape
        shape.setStateWith(this)
        return shape
    }

    companion object {
        val serializer get() = ShapeStateSerializer
    }
}
