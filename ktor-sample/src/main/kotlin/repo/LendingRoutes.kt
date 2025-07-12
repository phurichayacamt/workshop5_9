package repositories

import ex.LendingRecord
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LendingRecordRepository {
    private val lendingRecords = mutableListOf<LendingRecord>()
    private var nextId = 1
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun getAllLendingRecords(): List<LendingRecord> = lendingRecords

    fun getLendingRecordById(id: Int): LendingRecord? = lendingRecords.find { it.id == id }

    fun getLendingRecordsByBookId(bookId: Int): List<LendingRecord> = lendingRecords.filter { it.bookId == bookId }

    fun addLendingRecord(bookId: Int, borrowerName: String): LendingRecord {
        val record = LendingRecord(nextId++, bookId, borrowerName, LocalDateTime.now().format(formatter))
        lendingRecords.add(record)
        return record
    }

    fun updateLendingRecord(id: Int, bookId: Int, borrowerName: String): LendingRecord? {
        return lendingRecords.find { it.id == id }?.apply {
            val updatedRecord = LendingRecord(id, bookId, borrowerName, checkoutDate, returnDate)
            lendingRecords[lendingRecords.indexOf(this)] = updatedRecord
        }
    }

    fun deleteLendingRecord(id: Int): Boolean = lendingRecords.removeIf { it.id == id }

    fun returnBook(id: Int): Boolean {
        return lendingRecords.find { it.id == id && it.returnDate == null }?.apply {
            returnDate = LocalDateTime.now().format(formatter)
        } != null
    }

    fun getActiveLendingRecords(): List<LendingRecord> = lendingRecords.filter { it.returnDate == null }
}
//Please use port 8081 because port 8080 has a problem with me. No matter how much I kill it, it won't die.