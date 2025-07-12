package ex

import kotlinx.serialization.Serializable

@Serializable
data class LendingRecord(
    val id: Int,
    val bookId: Int,
    val borrowerName: String,
    val checkoutDate: String,
    var returnDate: String? = null
)
//Please use port 8081 because port 8080 has a problem with me. No matter how much I kill it, it won't die.