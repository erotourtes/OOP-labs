package com.github.erotourtes.drawing.shape

import com.github.erotourtes.utils.Dimension
import javafx.scene.paint.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class ShapeState(
    val className: String,
    val dm: Dimension,
    val colorFill: Color,
    val colorStroke: Color,
) {
    fun toShape(gc: javafx.scene.canvas.GraphicsContext): Shape {
        val shape = Class.forName(className).getConstructor(javafx.scene.canvas.GraphicsContext::class.java)
            .newInstance(gc) as Shape
        shape.setDm(dm)
        shape.colorFill = colorFill
        shape.colorStroke = colorStroke
        return shape
    }

    @Serializer(forClass = ShapeState::class)
    object serializer : KSerializer<ShapeState> {
        override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ShapeState") {
            element("className", String.serializer().descriptor)
            element("dm", String.serializer().descriptor)
            element("colorFill", String.serializer().descriptor)
            element("colorStroke", String.serializer().descriptor)
        }

        override fun deserialize(decoder: Decoder): ShapeState {
            val composite = decoder.beginStructure(descriptor)
            var className: String? = null
            var dm: String? = null
            var colorFill: String? = null
            var colorStroke: String? = null

            loop@ while (true) {
                when (val i = composite.decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break@loop
                    0 -> className = composite.decodeStringElement(descriptor, i)
                    1 -> dm = composite.decodeStringElement(descriptor, i)
                    2 -> colorFill = composite.decodeStringElement(descriptor, i)
                    3 -> colorStroke = composite.decodeStringElement(descriptor, i)
                    else -> throw IllegalArgumentException("Unexpected index: $i")
                }
            }

            composite.endStructure(descriptor)

            return ShapeState(
                className!!,
                Dimension().apply {
                    val (x1, y1, x2, y2) = dm!!.split(",").map { it.toDouble() }
                    setStart(x1, y1)
                    setEnd(x2, y2)
                },
                Color.valueOf(colorFill!!),
                Color.valueOf(colorStroke!!),
            )
        }

        override fun serialize(encoder: Encoder, value: ShapeState) {
            val composite = encoder.beginStructure(descriptor)
            composite.encodeStringElement(descriptor, 0, value.className)
            composite.encodeStringElement(
                descriptor,
                1,
                value.dm.getRaw().toList().joinToString(",") { (x, y) -> "$x,$y" }
            )
            composite.encodeStringElement(descriptor, 2, value.colorFill.toString())
            composite.encodeStringElement(descriptor, 3, value.colorStroke.toString())
            composite.endStructure(descriptor)
        }
    }
}
