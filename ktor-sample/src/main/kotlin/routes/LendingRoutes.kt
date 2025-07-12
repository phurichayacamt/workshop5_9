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
            call.respond(HttpStatusCode.OK, libraryService.getAllLendingRecords())
        }

        get("/{id}") {
            call.parameters["id"]?.toIntOrNull()?.let { id ->
                libraryService.getLendingRecordById(id)?.let {
                    call.respond(HttpStatusCode.OK, it)
                } ?: call.respond(HttpStatusCode.NotFound, "Lending record not found")
            } ?: call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
        }

        get("/book/{bookId}") {
            call.parameters["bookId"]?.toIntOrNull()?.let { bookId ->
                call.respond(HttpStatusCode.OK, libraryService.getLendingRecordsByBookId(bookId))
            } ?: call.respond(HttpStatusCode.BadRequest, "Invalid book ID")
        }

        get("/active") {
            call.respond(HttpStatusCode.OK, libraryService.getActiveLendingRecords())
        }

        post {
            try {
                val request = call.receive<CreateLendingRequest>()
                libraryService.addLendingRecord(request.bookId, request.borrowerName)?.let {
                    call.respond(HttpStatusCode.Created, it)
                } ?: call.respond(HttpStatusCode.BadRequest, "Book not found or not available")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request data")
            }
        }

        put("/{id}") {
            call.parameters["id"]?.toIntOrNull()?.let { id ->
                try {
                    val request = call.receive<UpdateLendingRequest>()
                    libraryService.updateLendingRecord(id, request.bookId, request.borrowerName)?.let {
                        call.respond(HttpStatusCode.OK, it)
                    } ?: call.respond(HttpStatusCode.NotFound, "Lending record not found")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request data")
                }
            } ?: call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
        }

        delete("/{id}") {
            call.parameters["id"]?.toIntOrNull()?.let { id ->
                if (libraryService.deleteLendingRecord(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Lending record not found")
                }
            } ?: call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
        }

        post("/{id}/return") {
            call.parameters["id"]?.toIntOrNull()?.let { id ->
                if (libraryService.returnBook(id)) {
                    call.respond(HttpStatusCode.OK, "Book returned successfully")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Lending record not found or book already returned")
                }
            } ?: call.respond(HttpStatusCode.BadRequest, "Invalid lending record ID")
        }
    }
}
