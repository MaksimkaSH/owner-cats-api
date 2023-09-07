package com.cats.model

import com.cats.serializers.LocalDateSerializer
import com.cats.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
class Owner(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID?,
    val name: String,
    @Serializable(with = LocalDateSerializer::class)
    val birthday: LocalDate
)