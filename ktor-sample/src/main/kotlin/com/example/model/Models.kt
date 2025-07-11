package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val isAvailable: Boolean
)

@Serializable
data class LendingRecord(
    val id: Int,
    val bookId: Int,
    val borrowerName: String,
    val checkoutDate: String,
    val returnDate: String? = null
)