package com.cats.model

import com.cats.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class Id(
    @Serializable(with = UUIDSerializer::class)
    val id:UUID?
)