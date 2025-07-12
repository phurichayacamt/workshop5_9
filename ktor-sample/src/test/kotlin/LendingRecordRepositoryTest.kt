import kotlin.test.*
import repositories.LendingRecordRepository

class LendingRecordRepositoryTest {
    private lateinit var lendingRecordRepository: LendingRecordRepository

    @BeforeTest
    fun setup() {
        lendingRecordRepository = LendingRecordRepository()
    }

    @Test
    fun `test getAllLendingRecords returns empty list initially`() {
        val records = lendingRecordRepository.getAllLendingRecords()
        assertTrue(records.isEmpty())
    }

    @Test
    fun `test addLendingRecord creates new record`() {
        val record = lendingRecordRepository.addLendingRecord(1, "John Doe")
        assertEquals(1, record.id)
        assertEquals(1, record.bookId)
        assertEquals("John Doe", record.borrowerName)
        assertNotNull(record.checkoutDate)
        assertNull(record.returnDate)
    }

    @Test
    fun `test getLendingRecordById returns correct record`() {
        val addedRecord = lendingRecordRepository.addLendingRecord(1, "John Doe")
        val record = lendingRecordRepository.getLendingRecordById(addedRecord.id)
        assertNotNull(record)
        assertEquals(addedRecord.id, record.id)
        assertEquals("John Doe", record.borrowerName)
    }

    @Test
    fun `test getLendingRecordById returns null for non-existent record`() {
        val record = lendingRecordRepository.getLendingRecordById(999)
        assertNull(record)
    }

    @Test
    fun `test getLendingRecordsByBookId returns correct records`() {
        lendingRecordRepository.addLendingRecord(1, "John Doe")
        lendingRecordRepository.addLendingRecord(1, "Jane Smith")
        lendingRecordRepository.addLendingRecord(2, "Bob Johnson")

        val records = lendingRecordRepository.getLendingRecordsByBookId(1)
        assertEquals(2, records.size)
        assertTrue(records.all { it.bookId == 1 })
    }

    @Test
    fun `test updateLendingRecord modifies existing record`() {
        val addedRecord = lendingRecordRepository.addLendingRecord(1, "John Doe")
        val updatedRecord = lendingRecordRepository.updateLendingRecord(addedRecord.id, 2, "Jane Smith")

        assertNotNull(updatedRecord)
        assertEquals(2, updatedRecord.bookId)
        assertEquals("Jane Smith", updatedRecord.borrowerName)
    }

    @Test
    fun `test updateLendingRecord returns null for non-existent record`() {
        val updatedRecord = lendingRecordRepository.updateLendingRecord(999, 1, "John Doe")
        assertNull(updatedRecord)
    }

    @Test
    fun `test deleteLendingRecord removes record successfully`() {
        val addedRecord = lendingRecordRepository.addLendingRecord(1, "John Doe")
        val initialCount = lendingRecordRepository.getAllLendingRecords().size

        val success = lendingRecordRepository.deleteLendingRecord(addedRecord.id)
        assertTrue(success)
        assertEquals(initialCount - 1, lendingRecordRepository.getAllLendingRecords().size)
    }

    @Test
    fun `test deleteLendingRecord returns false for non-existent record`() {
        val success = lendingRecordRepository.deleteLendingRecord(999)
        assertFalse(success)
    }

    @Test
    fun `test returnBook sets return date`() {
        val addedRecord = lendingRecordRepository.addLendingRecord(1, "John Doe")
        assertNull(addedRecord.returnDate)

        val success = lendingRecordRepository.returnBook(addedRecord.id)
        assertTrue(success)

        val returnedRecord = lendingRecordRepository.getLendingRecordById(addedRecord.id)
        assertNotNull(returnedRecord?.returnDate)
    }

    @Test
    fun `test returnBook returns false for non-existent record`() {
        val success = lendingRecordRepository.returnBook(999)
        assertFalse(success)
    }

    @Test
    fun `test getActiveLendingRecords returns only unreturned records`() {
        val record1 = lendingRecordRepository.addLendingRecord(1, "John Doe")
        val record2 = lendingRecordRepository.addLendingRecord(2, "Jane Smith")

        lendingRecordRepository.returnBook(record1.id)

        val activeRecords = lendingRecordRepository.getActiveLendingRecords()
        assertEquals(1, activeRecords.size)
        assertEquals(record2.id, activeRecords[0].id)
    }
} 