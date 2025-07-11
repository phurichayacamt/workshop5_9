package com.example.ktor.routes

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import com.example.ktor.LibraryService
import com.example.ktor.models.LendingRecord

fun Application.registerLendingRoutes(service: LibraryService) {
    routing {
        route("/lendings") {
            get {
                call.respond(service.getAllLendings())
            }
            post {
                val params = call.receive<LendingRecord>()
                val lending = service.lendBook(params.bookId, params.borrowerName)
                call.respond(lending)
            }
            get("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@get call.respondText("Invalid id", status = io.ktor.http.HttpStatusCode.BadRequest)
                service.getLending(id)?.let { call.respond(it) }
                    ?: call.respondText("Lending record not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
            put("{id}/return") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respondText("Invalid id", status = io.ktor.http.HttpStatusCode.BadRequest)
                try {
                    call.respond(service.returnBook(id))
                } catch (e: IllegalStateException) {
                    call.respondText(e.message ?: "Error", status = io.ktor.http.HttpStatusCode.BadRequest)
                }
            }
            delete("{id}") {
                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respondText("Invalid id", status = io.ktor.http.HttpStatusCode.BadRequest)
                if (service.deleteLending(id)) call.respondText("Lending record deleted")
                else call.respondText("Lending record not found", status = io.ktor.http.HttpStatusCode.NotFound)
            }
        }
    }
}
