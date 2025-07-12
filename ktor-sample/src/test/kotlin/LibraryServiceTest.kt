import kotlin.test.*
import repositories.BookRepository
import repositories.LendingRecordRepository
import services.LibraryService

class LibraryServiceTest {
    private lateinit var libraryService: LibraryService
    private lateinit var bookRepository: BookRepository
    private lateinit var lendingRecordRepository: LendingRecordRepository

    @BeforeTest
    fun setup() {
        bookRepository = BookRepository()
        lendingRecordRepository = LendingRecordRepository()
        libraryService = LibraryService(bookRepository, lendingRecordRepository)
    }

    @Test
    fun `test addLendingRecord with available book should succeed`() {
        val book = bookRepository.getBookById(1)
        assertTrue(book!!.isAvailable)

        val record = libraryService.addLendingRecord(1, "John Doe")
        assertNotNull(record)
        assertEquals(1, record.bookId)
        assertEquals("John Doe", record.borrowerName)

        // Check that book availability was updated
        val updatedBook = bookRepository.getBookById(1)
        assertFalse(updatedBook!!.isAvailable)
    }

    @Test
    fun `test addLendingRecord with unavailable book should fail`() {
        // First, borrow the book
        libraryService.addLendingRecord(1, "John Doe")

        // Try to borrow the same book again
        val record = libraryService.addLendingRecord(1, "Jane Smith")
        assertNull(record)
    }

    @Test
    fun `test addLendingRecord with non-existent book should fail`() {
        val record = libraryService.addLendingRecord(999, "John Doe")
        assertNull(record)
    }

    @Test
    fun `test returnBook should update book availability`() {
        // First, borrow a book
        val lendingRecord = libraryService.addLendingRecord(1, "John Doe")
        assertNotNull(lendingRecord)

        // Check that book is unavailable
        val book = bookRepository.getBookById(1)
        assertFalse(book!!.isAvailable)

        // Return the book
        val success = libraryService.returnBook(lendingRecord.id)
        assertTrue(success)

        // Check that book is available again
        val updatedBook = bookRepository.getBookById(1)
        assertTrue(updatedBook!!.isAvailable)
    }

    @Test
    fun `test returnBook with non-existent record should fail`() {
        val success = libraryService.returnBook(999)
        assertFalse(success)
    }

    @Test
    fun `test returnBook with already returned record should fail`() {
        // First, borrow and return a book
        val lendingRecord = libraryService.addLendingRecord(1, "John Doe")
        libraryService.returnBook(lendingRecord!!.id)

        // Try to return the same record again
        val success = libraryService.returnBook(lendingRecord.id)
        assertFalse(success)
    }

    @Test
    fun `test getAllBooks returns all books`() {
        val books = libraryService.getAllBooks()
        assertTrue(books.isNotEmpty())
        assertEquals(3, books.size)
    }

    @Test
    fun `test getBookById returns correct book`() {
        val book = libraryService.getBookById(1)
        assertNotNull(book)
        assertEquals("The Great Gatsby", book.title)
    }

    @Test
    fun `test addBook creates new book`() {
        val book = libraryService.addBook("Test Book", "Test Author")
        assertEquals("Test Book", book.title)
        assertEquals("Test Author", book.author)
        assertTrue(book.isAvailable)
    }

    @Test
    fun `test updateBook modifies existing book`() {
        val updatedBook = libraryService.updateBook(1, "Updated Title", "Updated Author")
        assertNotNull(updatedBook)
        assertEquals("Updated Title", updatedBook.title)
        assertEquals("Updated Author", updatedBook.author)
    }

    @Test
    fun `test deleteBook removes book`() {
        val initialCount = libraryService.getAllBooks().size
        val success = libraryService.deleteBook(1)
        assertTrue(success)
        assertEquals(initialCount - 1, libraryService.getAllBooks().size)
    }

    @Test
    fun `test getAllLendingRecords returns all records`() {
        libraryService.addLendingRecord(1, "John Doe")
        libraryService.addLendingRecord(2, "Jane Smith")

        val records = libraryService.getAllLendingRecords()
        assertEquals(2, records.size)
    }

    @Test
    fun `test getActiveLendingRecords returns only unreturned records`() {
        val record1 = libraryService.addLendingRecord(1, "John Doe")
        val record2 = libraryService.addLendingRecord(2, "Jane Smith")

        libraryService.returnBook(record1!!.id)

        val activeRecords = libraryService.getActiveLendingRecords()
        assertEquals(1, activeRecords.size)
        assertEquals(record2!!.id, activeRecords[0].id)
    }
} //Please use port 8081 because port 8080 has a problem with me. No matter how much I kill it, it won't die.