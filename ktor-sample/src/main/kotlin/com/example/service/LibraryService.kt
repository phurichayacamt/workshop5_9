package com.example.service

import com.example.model.LendingRecord
import com.example.repository.LendingRepository
import org.jetbrains.exposed.sql.transactions.transaction

class LibraryService(
    private val bookRepo: BookRepository,
    private val lendRepo: LendingRepository
) {
    fun lendBook(record: LendingRecord): LendingRecord = transaction {
        val book = bookRepo.getById(record.bookId)
            ?: throw IllegalArgumentException("Book not found")
        if (!book.isAvailable) throw IllegalStateException("Book is already lent out")
        val newRecord = lendRepo.add(record)
        bookRepo.setAvailability(record.bookId, false)
        newRecord
    }

    fun returnBook(lendingId: Int, returnDate: String): LendingRecord = transaction {
        val record = lendRepo.getById(lendingId)
            ?: throw IllegalArgumentException("Lending record not found")
        lendRepo.updateReturnDate(lendingId, returnDate)
        bookRepo.setAvailability(record.bookId, true)
        lendRepo.getById(lendingId)!!
    }
}