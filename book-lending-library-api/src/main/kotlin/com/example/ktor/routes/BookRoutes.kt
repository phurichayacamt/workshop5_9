package com.example.ktor.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import com.example.ktor.LibraryService
import com.example.ktor.models.Book

fun Application.registerBookRoutes(service: LibraryService) {
    routing {
        route("/books") {
            get {
                call.respond(service.getAllBooks())
            }
            post {
                val book = call.receive<Book>()
                call.respond(service.addBook(book))
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText("Invalid id", status = io.ktor.http.HttpStatusCode.BadRequest)
                service.getBook(id)?.let { call.respond(it) }
                    ?: call.respondText("Book not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
            put("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respondText("Invalid id", status = io.ktor.http.HttpStatusCode.BadRequest)
                val book = call.receive<Book>()
                service.updateBook(id, book)?.let { call.respond(it) }
                    ?: call.respondText("Book not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
            delete("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText("Invalid id", status = io.ktor.http.HttpStatusCode.BadRequest)
                if (service.deleteBook(id)) call.respondText("Book deleted") else call.respondText("Book not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }
    }
}
