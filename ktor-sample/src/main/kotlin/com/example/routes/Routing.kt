// src/main/kotlin/com/example/DatabaseFactory.kt
package com.example.com.example.routes

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = System.getenv("JDBC_DATABASE_URL")
                ?: "jdbc:postgresql://localhost:5432/library"
            driverClassName = "org.postgresql.Driver"
            username = System.getenv("DB_USER") ?: "postgres"
            password = System.getenv("DB_PASSWORD") ?: "password"
            maximumPoolSize = 5
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
        // สร้างตาราง
        transaction {
            SchemaUtils.create(BooksTable, LendingRecordsTable)
        }
    }
}
