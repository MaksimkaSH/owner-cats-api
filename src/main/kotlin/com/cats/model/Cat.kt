package com.cats.model

import com.cats.serializers.LocalDateSerializer
import com.cats.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.UUID

@Serializable
data class Cat(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID?,
    val name: String,
    val color: String,
    @Serializable(with = UUIDSerializer::class)
    val owner: UUID?,
    @Serializable(with = LocalDateSerializer::class)
    val birthday: LocalDate
)