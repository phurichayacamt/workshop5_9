package com.example.repository

import com.example.model.Book
import com.example.model.BooksTable
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*

// ... class BookRepository { ...

fun add(book: Book): Book = transaction {
    // ตอนนี้ insertAndGetId จะใช้งานได้เลย
    val newId = BooksTable.insertAndGetId {
        it[title]       = book.title
        it[author]      = book.author
        it[isAvailable] = book.isAvailable
    }.value

    // ดึงกลับมาเป็น Book
    getById(newId)!!
}

fun getById(id: Int): Book? = transaction {
    BooksTable.select { BooksTable.id eq id }
        .map {
            Book(
                id          = it[BooksTable.id].value,
                title       = it[BooksTable.title],
                author      = it[BooksTable.author],
                isAvailable = it[BooksTable.isAvailable]
            )
        }
        .singleOrNull()
}

// ...
