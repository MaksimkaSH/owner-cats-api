package com.cats.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

object UUIDSerializer : KSerializer<UUID?> {
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): UUID? {
        return try {
            UUID.fromString(decoder.decodeString())
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun serialize(encoder: Encoder, value: UUID?) {
        encoder.encodeString(value?.toString() ?: "")
    }
}