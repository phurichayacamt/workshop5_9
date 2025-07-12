import kotlin.test.*
import repositories.BookRepository

class BookRepositoryTest {
    private lateinit var bookRepository: BookRepository

    @BeforeTest
    fun setup() {
        bookRepository = BookRepository()
    }

    @Test
    fun `test getAllBooks returns all books`() {
        val books = bookRepository.getAllBooks()
        assertTrue(books.isNotEmpty())
        assertEquals(3, books.size) // 3 sample books from init
    }

    @Test
    fun `test getBookById returns correct book`() {
        val book = bookRepository.getBookById(1)
        assertEquals("The Great Gatsby", book?.title)
        assertEquals("F. Scott Fitzgerald", book?.author)
    }

    @Test
    fun `test getBookById returns null for non-existent book`() {
        assertNull(bookRepository.getBookById(999))
    }

    @Test
    fun `test addBook creates new book with correct data`() {
        val newBook = bookRepository.addBook("Test Book", "Test Author")
        assertEquals("Test Book", newBook.title)
        assertEquals("Test Author", newBook.author)
        assertTrue(newBook.isAvailable)
    }

    @Test
    fun `test updateBook modifies existing book`() {
        val updatedBook = bookRepository.updateBook(1, "Updated Title", "Updated Author")
        assertEquals("Updated Title", updatedBook?.title)
        assertEquals("Updated Author", updatedBook?.author)
    }

    @Test
    fun `test updateBook returns null for non-existent book`() {
        assertNull(bookRepository.updateBook(999, "Updated Title", "Updated Author"))
    }

    @Test
    fun `test deleteBook removes book successfully`() {
        val initialCount = bookRepository.getAllBooks().size
        assertTrue(bookRepository.deleteBook(1))
        assertEquals(initialCount - 1, bookRepository.getAllBooks().size)
    }

    @Test
    fun `test deleteBook returns false for non-existent book`() {
        assertFalse(bookRepository.deleteBook(999))
    }

    @Test
    fun `test updateBookAvailability changes availability status`() {
        val book = bookRepository.getBookById(1)!!
        assertTrue(book.isAvailable)

        assertTrue(bookRepository.updateBookAvailability(1, false))
        assertFalse(bookRepository.getBookById(1)?.isAvailable ?: true)
    }
}
