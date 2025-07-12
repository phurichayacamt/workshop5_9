package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import services.LibraryService

@Serializable
data class CreateLendingRequest(val bookId: Int, val borrowerName: String)

@Serializable
data class UpdateLendingRequest(val bookId: Int, val borrowerName: String)

fun Route.lendingRoutes(libraryService: LibraryService) {
    route("/lending") {

        get {
            val records = libraryService.getAllLendingRecords()
            call.respond(HttpStatusCode.OK, records)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
                return@get
            }

            val record = libraryService.getLendingRecordById(id)
            if (record == null) {
                call.respond(HttpStatusCode.NotFound, "Lending record not found")
            } else {
                call.respond(HttpStatusCode.OK, record)
            }
        }

        get("/book/{bookId}") {
            val bookId = call.parameters["bookId"]?.toIntOrNull()
            if (bookId == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid book ID")
                return@get
            }

            val records = libraryService.getLendingRecordsByBookId(bookId)
            call.respond(HttpStatusCode.OK, records)
        }

        get("/active") {
            val records = libraryService.getActiveLendingRecords()
            call.respond(HttpStatusCode.OK, records)
        }

        post {
            try {
                val request = call.receive<CreateLendingRequest>()
                val record = libraryService.addLendingRecord(request.bookId, request.borrowerName)
                if (record == null) {
                    call.respond(HttpStatusCode.BadRequest, "Book not found or not available")
                } else {
                    call.respond(HttpStatusCode.Created, record)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request data")
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
                return@put
            }

            try {
                val request = call.receive<UpdateLendingRequest>()
                val record = libraryService.updateLendingRecord(id, request.bookId, request.borrowerName)
                if (record == null) {
                    call.respond(HttpStatusCode.NotFound, "Lending record not found")
                } else {
                    call.respond(HttpStatusCode.OK, record)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request data")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
                return@delete
            }

            val success = libraryService.deleteLendingRecord(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Lending record not found")
            }
        }

        post("/{id}/return") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
                return@post
            }

            val success = libraryService.returnBook(id)
            if (success) {
                call.respond(HttpStatusCode.OK, "Book returned successfully")
            } else {
                call.respond(HttpStatusCode.BadRequest, "Lending record not found or book already returned")
            }
        }
    }
}