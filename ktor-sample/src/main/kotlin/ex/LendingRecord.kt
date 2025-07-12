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