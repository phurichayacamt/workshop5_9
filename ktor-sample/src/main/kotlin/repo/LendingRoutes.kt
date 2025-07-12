package repositories

import models.LendingRecord
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LendingRecordRepository {
    private val lendingRecords = mutableListOf<LendingRecord>()
    private var nextId = 1
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun getAllLendingRecords(): List<LendingRecord> = lendingRecords.toList()

    fun getLendingRecordById(id: Int): LendingRecord? = lendingRecords.find { it.id == id }

    fun getLendingRecordsByBookId(bookId: Int): List<LendingRecord> =
        lendingRecords.filter { it.bookId == bookId }

    fun addLendingRecord(bookId: Int, borrowerName: String): LendingRecord {
        val checkoutDate = LocalDateTime.now().format(formatter)
        val record = LendingRecord(nextId++, bookId, borrowerName, checkoutDate)
        lendingRecords.add(record)
        return record
    }

    fun updateLendingRecord(id: Int, bookId: Int, borrowerName: String): LendingRecord? {
        val record = lendingRecords.find { it.id == id }
        if (record != null) {
            val updatedRecord = LendingRecord(record.id, bookId, borrowerName, record.checkoutDate, record.returnDate)
            val index = lendingRecords.indexOf(record)
            lendingRecords[index] = updatedRecord
            return updatedRecord
        }
        return record
    }

    fun deleteLendingRecord(id: Int): Boolean {
        val record = lendingRecords.find { it.id == id }
        return if (record != null) {
            lendingRecords.remove(record)
            true
        } else {
            false
        }
    }

    fun returnBook(id: Int): Boolean {
        val record = lendingRecords.find { it.id == id }
        if (record != null && record.returnDate == null) {
            record.returnDate = LocalDateTime.now().format(formatter)
            return true
        }
        return false
    }

    fun getActiveLendingRecords(): List<LendingRecord> =
        lendingRecords.filter { it.returnDate == null }
}