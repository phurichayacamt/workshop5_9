package com.example.model

import org.jetbrains.exposed.dao.id.IntIdTable

// เปลี่ยนจาก `object BooksTable : Table(...)` มาเป็น IntIdTable
object BooksTable : IntIdTable("books") {
    // id ถูกสร้างให้อัตโนมัติ
    val title       = varchar("title", 255)
    val author      = varchar("author", 255)
    val isAvailable = bool("is_available").default(true)
}

object LendingRecordsTable : IntIdTable("lending_records") {
    // สร้าง FK reference ไปยัง BooksTable
    val book    = reference("book_id", BooksTable)
    val borrowerName = varchar("borrower_name", 255)
    val checkoutDate = varchar("checkout_date", 50)
    val returnDate   = varchar("return_date", 50).nullable()
}
