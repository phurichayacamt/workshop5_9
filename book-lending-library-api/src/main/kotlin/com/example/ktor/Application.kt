package com.example.ktor

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import com.example.ktor.routes.registerBookRoutes
import com.example.ktor.routes.registerLendingRoutes

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // เปิดใช้งาน logging ของ HTTP calls
    install(CallLogging)

    // เปิดใช้งาน JSON serialization
    install(ContentNegotiation) {
        json()
    }

    // สร้าง repository + service
    val repository = LibraryRepository()
    val service = LibraryService(repository)

    // ลงทะเบียน REST routes
    registerBookRoutes(service)
    registerLendingRoutes(service)
}
