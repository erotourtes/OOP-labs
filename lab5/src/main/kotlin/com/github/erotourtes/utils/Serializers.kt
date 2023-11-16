package com.github.erotourtes.utils

import com.github.erotourtes.drawing.GCState
import com.github.erotourtes.drawing.shape.ShapeState
import javafx.scene.paint.Color
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializer(forClass = ShapeState::class)
object ShapeStateSerializer : KSerializer<ShapeState> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ShapeState") {
        element("className", String.serializer().descriptor)
        element("dm", Dimension.serializer.descriptor)
        element("gcState", GCState.serializer.descriptor)
    }

    override fun deserialize(decoder: Decoder): ShapeState {
        val composite = decoder.beginStructure(descriptor)
        var className: String? = null
        var dm: Dimension? = null
        var gcState: GCState? = null

        loop@ while (true) {
            when (val i = composite.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> className = composite.decodeStringElement(descriptor, i)
                1 -> dm = composite.decodeSerializableElement(descriptor, i, Dimension.serializer)
                2 -> gcState =
                    composite.decodeSerializableElement(descriptor, i, GCState.serializer)

                else -> throw IllegalArgumentException("Unexpected index: $i")
            }
        }

        composite.endStructure(descriptor)

        return ShapeState(
            className!!,
            dm!!,
            gcState!!
        )
    }

    override fun serialize(encoder: Encoder, value: ShapeState) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeStringElement(descriptor, 0, value.clazz)
        composite.encodeSerializableElement(descriptor, 1, Dimension.serializer, value.dm)
        composite.encodeSerializableElement(descriptor, 2, GCState.serializer, value.gcState)
        composite.endStructure(descriptor)
    }
}

@Serializer(forClass = ShapeState::class)
object GCStateSerializer : KSerializer<GCState> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("GCState") {
        element("fill", String.serializer().descriptor)
        element("stroke", String.serializer().descriptor)
        element("lineWidth", Double.serializer().descriptor)
    }

    override fun deserialize(decoder: Decoder): GCState {
        val composite = decoder.beginStructure(descriptor)

        var fill: String? = null
        var stroke: String? = null
        var lineWidth: Double? = null

        loop@ while (true) {
            when (val i = composite.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> fill = composite.decodeStringElement(descriptor, i)
                1 -> stroke = composite.decodeStringElement(descriptor, i)
                2 -> lineWidth = composite.decodeDoubleElement(descriptor, i)
                else -> throw IllegalArgumentException("Unexpected index: $i")
            }
        }

        composite.endStructure(descriptor)

        return GCState(
            Color.valueOf(fill!!),
            Color.valueOf(stroke!!),
            lineWidth!!,
        )
    }

    override fun serialize(encoder: Encoder, value: GCState) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeStringElement(descriptor, 0, value.fill.toString())
        composite.encodeStringElement(descriptor, 1, value.stroke.toString())
        composite.encodeDoubleElement(descriptor, 2, value.lineWidth)
        composite.endStructure(descriptor)
    }
}

@Serializer(forClass = Dimension::class)
object DimensionSerializer : KSerializer<Dimension> {
    override val descriptor: SerialDescriptor = ListSerializer(Double.serializer()).descriptor

    override fun deserialize(decoder: Decoder): Dimension {
        val list = decoder.decodeSerializableValue(ListSerializer(Double.serializer()))

        return Dimension().apply {
            val (x1, y1, x2, y2) = list
            setStart(x1, y1)
            setEnd(x2, y2)
        }
    }

    override fun serialize(encoder: Encoder, value: Dimension) {
        val list = value.getRaw().toList().map { (x, y) -> listOf(x, y) }.flatten()
        encoder.encodeSerializableValue(ListSerializer(Double.serializer()), list)
    }
}