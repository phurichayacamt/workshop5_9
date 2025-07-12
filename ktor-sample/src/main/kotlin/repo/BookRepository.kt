package repositories

import ex.Book

class BookRepository {
    private val books = mutableListOf(
        Book(1, "The Whispering Shadows", "Luna Nightshade"),
        Book(2, "Electric Dreams and Rusted Realities", "Neo Virgil"),
        Book(3, "Starlight and Asphalt", "Orion Valentina")

    )
    private var nextId = 4

    fun getAllBooks(): List<Book> = books

    fun getBookById(id: Int): Book? = books.find { it.id == id }

    fun addBook(title: String, author: String): Book {
        val book = Book(nextId++, title, author)
        books.add(book)
        return book
    }

    fun updateBook(id: Int, title: String, author: String): Book? {
        val book = books.find { it.id == id } ?: return null
        val updatedBook = book.copy(title = title, author = author)
        books[books.indexOf(book)] = updatedBook
        return updatedBook
    }

    fun deleteBook(id: Int): Boolean = books.removeIf { it.id == id }

    fun updateBookAvailability(id: Int, isAvailable: Boolean): Boolean {
        val book = books.find { it.id == id } ?: return false
        book.isAvailable = isAvailable
        return true
    }
}

