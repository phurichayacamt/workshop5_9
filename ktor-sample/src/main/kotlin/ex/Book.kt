package ex

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    var isAvailable: Boolean = true
)