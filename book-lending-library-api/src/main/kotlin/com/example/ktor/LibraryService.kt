package com.example.ktor

import com.example.ktor.models.Book
import com.example.ktor.models.LendingRecord
import java.time.LocalDateTime

class LibraryService(private val repo: LibraryRepository) {
    fun getAllBooks(): List<Book> = repo.getAllBooks()
    fun getBook(id: Int): Book? = repo.getBook(id)
    fun addBook(book: Book): Book = repo.addBook(book)
    fun updateBook(id: Int, book: Book): Book? = repo.updateBook(id, book)
    fun deleteBook(id: Int): Boolean = repo.deleteBook(id)
    fun getAllLendings(): List<LendingRecord> = repo.getAllLendings()
    fun getLending(id: Int): LendingRecord? = repo.getLending(id)
    fun lendBook(bookId: Int, borrowerName: String): LendingRecord {
        val book = repo.getBook(bookId) ?: throw IllegalArgumentException("Book not found")
        if (!book.isAvailable) throw IllegalStateException("Book is not available")
        repo.updateBook(bookId, book.copy(isAvailable = false))
        val record = LendingRecord(
            bookId = bookId,
            borrowerName = borrowerName,
            checkoutDate = LocalDateTime.now()
        )
        return repo.addLending(record)
    }
    fun returnBook(lendingId: Int): LendingRecord {
        val record = repo.getLending(lendingId) ?: throw IllegalArgumentException("Lending record not found")
        if (record.returnDate != null) throw IllegalStateException("Book already returned")
        val returnedRecord = record.copy(returnDate = LocalDateTime.now())
        repo.updateLending(lendingId, returnedRecord)
        val book = repo.getBook(record.bookId) ?: throw IllegalStateException("Book not found")
        repo.updateBook(record.bookId, book.copy(isAvailable = true))
        return returnedRecord
    }
    fun deleteLending(id: Int): Boolean {
        val record = repo.getLending(id)
        if (record != null && record.returnDate == null) {
            val book = repo.getBook(record.bookId)
            if (book != null) {
                repo.updateBook(record.bookId, book.copy(isAvailable = true))
            }
        }
        return repo.deleteLending(id)
    }///
}
