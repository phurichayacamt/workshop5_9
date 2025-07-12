package ex

import kotlinx.serialization.Serializable

@Serializable
data class Book(
    val id: Int,
    val title: String,
    val author: String,
    var isAvailable: Boolean = true
)
//Please use port 8081 because port 8080 has a problem with me. No matter how much I kill it, it won't die.