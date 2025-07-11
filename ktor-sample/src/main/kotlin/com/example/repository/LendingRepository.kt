package com.example.repository

import com.example.model.BooksTable
import com.example.model.LendingRecord
import com.example.model.LendingRecordsTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class LendingRepository {
    fun add(record: LendingRecord): LendingRecord = transaction {
        val newId = LendingRecordsTable.insertAndGetId {
            // ต้องสร้าง EntityID ให้ตรงกับ IntIdTable
            it[book] = EntityID(record.bookId, BooksTable)
            it[borrowerName] = record.borrowerName
            it[checkoutDate] = record.checkoutDate
            it[returnDate] = record.returnDate
        }.value

        getById(newId)!!
    }

    fun getById(id: Int): LendingRecord? = transaction {
        LendingRecordsTable.select { LendingRecordsTable.id eq id }
            .map {
                LendingRecord(
                    id = it[LendingRecordsTable.id].value,
                    bookId = it[LendingRecordsTable.book].value,
                    borrowerName = it[LendingRecordsTable.borrowerName],
                    checkoutDate = it[LendingRecordsTable.checkoutDate],
                    returnDate = it[LendingRecordsTable.returnDate]
                )
            }
            .singleOrNull()
    }

    // ... ฟังก์ชันอื่นๆ ...
}