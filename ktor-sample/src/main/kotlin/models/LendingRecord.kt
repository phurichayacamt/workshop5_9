package models

import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class LendingRecord(
    val id: Int,
    val bookId: Int,
    val borrowerName: String,
    val checkoutDate: String,
    var returnDate: String? = null
)