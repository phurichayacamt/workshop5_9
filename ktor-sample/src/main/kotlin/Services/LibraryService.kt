package services

import ex.Book
import ex.LendingRecord
import repositories.BookRepository
import repositories.LendingRecordRepository

class LibraryService(
    private val bookRepository: BookRepository,
    private val lendingRecordRepository: LendingRecordRepository
) {


    fun getAllBooks(): List<Book> = bookRepository.getAllBooks()

    fun getBookById(id: Int): Book? = bookRepository.getBookById(id)

    fun addBook(title: String, author: String): Book =
        bookRepository.addBook(title, author)

    fun updateBook(id: Int, title: String, author: String): Book? =
        bookRepository.updateBook(id, title, author)

    fun deleteBook(id: Int): Boolean = bookRepository.deleteBook(id)


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


        val record = lendingRecordRepository.addLendingRecord(bookId, borrowerName)


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
            return false
        }


        val success = lendingRecordRepository.returnBook(lendingRecordId)
        if (success) {
            bookRepository.updateBookAvailability(record.bookId, true)
        }

        return success
    }

    fun getActiveLendingRecords(): List<LendingRecord> =
        lendingRecordRepository.getActiveLendingRecords()
}
//Please use port 8081 because port 8080 has a problem with me. No matter how much I kill it, it won't die.