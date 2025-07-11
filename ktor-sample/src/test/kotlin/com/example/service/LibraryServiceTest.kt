package com.example.service

import com.example.model.Book
import com.example.model.BooksTable
import com.example.model.LendingRecord
import com.example.model.LendingRecordsTable
import com.example.repository.LendingRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class LibraryServiceTest {
    private lateinit var bookRepo: BookRepository
    private lateinit var lendRepo: LendingRepository
    private lateinit var service: LibraryService

    @BeforeTest
    fun setup() {
        Database.Companion.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction { SchemaUtils.create(BooksTable, LendingRecordsTable) }
        bookRepo = BookRepository()
        lendRepo = LendingRepository()
        service  = LibraryService(bookRepo, lendRepo)
    }

    @Test
    fun `lend available book`() {
        val book   = bookRepo.add(Book(0, "Kotlin in Action", "Dierk Konig", true))
        val record = LendingRecord(0, book.id, "Alice", "2025-07-05T10:00", null)
        val lent   = service.lendBook(record)

        assertFalse(bookRepo.getById(book.id)!!.isAvailable)
        assertEquals("Alice", lent.borrowerName)
    }

    @Test
    fun `cannot lend unavailable book`() {
        val book   = bookRepo.add(Book(0, "Clean Code", "Robert C. Martin", false))
        val record = LendingRecord(0, book.id, "Bob", "2025-07-05T11:00", null)
        assertFailsWith<IllegalStateException> { service.lendBook(record) }
    }

    @Test
    fun `return book`() {
        val book     = bookRepo.add(Book(0, "Refactoring", "Martin Fowler", true))
        val record   = service.lendBook(LendingRecord(0, book.id, "Charlie", "2025-07-05T12:00", null))
        val returned = service.returnBook(record.id, "2025-07-06T09:00")

        assertTrue(bookRepo.getById(book.id)!!.isAvailable)
        assertEquals("2025-07-06T09:00", returned.returnDate)
    }
}