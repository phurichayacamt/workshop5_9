package com.example.com.example.routes

import com.example.model.Book
import com.example.model.LendingRecord
import com.example.repository.BookRepository
import com.example.repository.LendingRepository
import com.example.service.LibraryService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route

fun Route.libraryRoutes() {
    val bookRepo   = BookRepository()
    val lendRepo   = LendingRepository()
    val service    = LibraryService(bookRepo, lendRepo)

    route("/books") {
        get    { call.respond(bookRepo.getAll()) }
        get("{id}") {
            val id = call.parameters["id"]!!.toInt()
            bookRepo.getById(id)
                ?.let { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound)
        }
        post   {
            val book = call.receive<Book>()
            call.respond(HttpStatusCode.Created, bookRepo.add(book))
        }
        put("{id}") {
            val id = call.parameters["id"]!!.toInt()
            val book = call.receive<Book>()
            if (bookRepo.update(id, book)) call.respond(HttpStatusCode.OK)
            else call.respond(HttpStatusCode.NotFound)
        }
        delete("{id}") {
            val id = call.parameters["id"]!!.toInt()
            if (bookRepo.delete(id)) call.respond(HttpStatusCode.NoContent)
            else call.respond(HttpStatusCode.NotFound)
        }
    }

    route("/lendings") {
        get    { call.respond(lendRepo.getAll()) }
        get("{id}") {
            val id = call.parameters["id"]!!.toInt()
            lendRepo.getById(id)
                ?.let { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound)
        }
        post   {
            val record = call.receive<LendingRecord>()
            try {
                call.respond(HttpStatusCode.Created, service.lendBook(record))
            } catch (e: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "")
            }
        }
        post("{id}/return") {
            val id = call.parameters["id"]!!.toInt()
            val payload = call.receive<Map<String, String>>()
            try {
                call.respond(service.returnBook(id, payload["returnDate"]!!))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.NotFound, e.message ?: "")
            }
        }
    }
}