package repositories

import models.Book

class BookRepository {
    private val books = mutableListOf<Book>()
    private var nextId = 1

    init {
        // เพิ่มข้อมูลตัวอย่าง
        books.add(Book(nextId++, "aaaaa", "king"))
        books.add(Book(nextId++, "ssss", "kingg"))
        books.add(Book(nextId++, "0000", "kingg"))
    }

    fun getAllBooks(): List<Book> = books.toList()

    fun getBookById(id: Int): Book? = books.find { it.id == id }

    fun addBook(title: String, author: String): Book {
        val book = Book(nextId++, title, author)
        books.add(book)
        return book
    }

    fun updateBook(id: Int, title: String, author: String): Book? {
        val book = books.find { it.id == id }
        if (book != null) {
            // Create a new book with updated values since title and author are val
            val updatedBook = Book(book.id, title, author, book.isAvailable)
            val index = books.indexOf(book)
            books[index] = updatedBook
            return updatedBook
        }
        return book
    }

    fun deleteBook(id: Int): Boolean {
        val book = books.find { it.id == id }
        return if (book != null) {
            books.remove(book)
            true
        } else {
            false
        }
    }

    fun updateBookAvailability(id: Int, isAvailable: Boolean): Boolean {
        val book = books.find { it.id == id }
        if (book != null) {
            book.isAvailable = isAvailable
            return true
        }
        return false
    }
}