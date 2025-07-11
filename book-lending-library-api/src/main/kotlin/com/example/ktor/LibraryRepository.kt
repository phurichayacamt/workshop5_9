package com.example.ktor

import com.example.ktor.models.Book
import com.example.ktor.models.LendingRecord
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class LibraryRepository {
    private val books = ConcurrentHashMap<Int, Book>()
    private val lendings = ConcurrentHashMap<Int, LendingRecord>()
    private val bookIdCounter = AtomicInteger()
    private val lendingIdCounter = AtomicInteger()

    fun getAllBooks(): List<Book> = books.values.toList()
    fun getBook(id: Int): Book? = books[id]
    fun addBook(book: Book): Book {
        val id = bookIdCounter.incrementAndGet()
        val newBook = book.copy(id = id)
        books[id] = newBook
        return newBook
    }
    fun updateBook(id: Int, book: Book): Book? {
        if (!books.containsKey(id)) return null
        val updated = book.copy(id = id)
        books[id] = updated
        return updated
    }
    fun deleteBook(id: Int): Boolean = books.remove(id) != null

    fun getAllLendings(): List<LendingRecord> = lendings.values.toList()
    fun getLending(id: Int): LendingRecord? = lendings[id]
    fun addLending(record: LendingRecord): LendingRecord {
        val id = lendingIdCounter.incrementAndGet()
        val newRecord = record.copy(id = id)
        lendings[id] = newRecord
        return newRecord
    }
    fun updateLending(id: Int, record: LendingRecord): LendingRecord? {
        if (!lendings.containsKey(id)) return null
        val updated = record.copy(id = id)
        lendings[id] = updated
        return updated
    }
    fun deleteLending(id: Int): Boolean = lendings.remove(id) != null
}
