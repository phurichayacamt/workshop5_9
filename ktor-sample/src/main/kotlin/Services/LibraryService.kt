package services

import models.Book
import models.LendingRecord
import repositories.BookRepository
import repositories.LendingRecordRepository

class LibraryService(
    private val bookRepository: BookRepository,
    private val lendingRecordRepository: LendingRecordRepository
) {

    // Book operations
    fun getAllBooks(): List<Book> = bookRepository.getAllBooks()

    fun getBookById(id: Int): Book? = bookRepository.getBookById(id)

    fun addBook(title: String, author: String): Book =
        bookRepository.addBook(title, author)

    fun updateBook(id: Int, title: String, author: String): Book? =
        bookRepository.updateBook(id, title, author)

    fun deleteBook(id: Int): Boolean = bookRepository.deleteBook(id)

    // Lending operations
    fun getAllLendingRecords(): List<LendingRecord> =
        lendingRecordRepository.getAllLendingRecords()

    fun getLendingRecordById(id: Int): LendingRecord? =
        lendingRecordRepository.getLendingRecordById(id)

    fun getLendingRecordsByBookId(bookId: Int): List<LendingRecord> =
        lendingRecordRepository.getLendingRecordsByBookId(bookId)

    fun addLendingRecord(bookId: Int, borrowerName: String): LendingRecord? {
        val book = bookRepository.getBookById(bookId)
        if (book == null) {
            return null // Book not found
        }

        if (!book.isAvailable) {
            return null // Book is not available
        }

        // Create lending record
        val record = lendingRecordRepository.addLendingRecord(bookId, borrowerName)

        // Update book availability
        bookRepository.updateBookAvailability(bookId, false)

        return record
    }

    fun updateLendingRecord(id: Int, bookId: Int, borrowerName: String): LendingRecord? =
        lendingRecordRepository.updateLendingRecord(id, bookId, borrowerName)

    fun deleteLendingRecord(id: Int): Boolean =
        lendingRecordRepository.deleteLendingRecord(id)

    fun returnBook(lendingRecordId: Int): Boolean {
        val record = lendingRecordRepository.getLendingRecordById(lendingRecordId)
        if (record == null || record.returnDate != null) {
            return false // Record not found or already returned
        }

        // Update return date
        val success = lendingRecordRepository.returnBook(lendingRecordId)
        if (success) {
            // Update book availability back to true
            bookRepository.updateBookAvailability(record.bookId, true)
        }

        return success
    }

    fun getActiveLendingRecords(): List<LendingRecord> =
        lendingRecordRepository.getActiveLendingRecords()
}