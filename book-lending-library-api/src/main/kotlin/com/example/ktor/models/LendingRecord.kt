package com.example.ktor.models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import com.example.ktor.LocalDateTimeSerializer

@Serializable
data class LendingRecord(
    val id: Int = 0,
    val bookId: Int,
    val borrowerName: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val checkoutDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val returnDate: LocalDateTime? = null
)
