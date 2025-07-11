package com.example.ktor

import kotlin.test.*
import org.junit.Test
import com.example.ktor.models.Book

class LibraryServiceTest {
    private val repo = LibraryRepository()
    private val service = LibraryService(repo)

    @Test
    fun testAddAndGetBook() {
        val book = service.addBook(Book(title = "Test", author = "Author"))
        assertNotNull(service.getBook(book.id))
        assertEquals("Test", service.getBook(book.id)?.title)
    }

    @Test
    fun testLendAndReturnBook() {
        val book = service.addBook(Book(title = "Test", author = "Author"))
        val lending = service.lendBook(book.id, "John")
        assertFalse(service.getBook(book.id)?.isAvailable ?: true)
        val returned = service.returnBook(lending.id)
        assertTrue(service.getBook(book.id)?.isAvailable ?: false)
        assertNotNull(returned.returnDate)
    }

    @Test
    fun testCannotLendUnavailableBook() {
        val book = service.addBook(Book(title = "Test", author = "Author"))
        service.lendBook(book.id, "John")
        assertFailsWith<IllegalStateException> {
            service.lendBook(book.id, "Jane")
        }
    }
}
