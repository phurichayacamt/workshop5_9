package routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import services.LibraryService

@Serializable
data class CreateBookRequest(val title: String, val author: String)

@Serializable
data class UpdateBookRequest(val title: String, val author: String)

fun Route.bookRoutes(libraryService: LibraryService) {
    route("/books") {

        get {
            call.respond(HttpStatusCode.OK, libraryService.getAllBooks())
        }

        get("/{id}") {
            call.parameters["id"]?.toIntOrNull()?.let { id ->
                libraryService.getBookById(id)?.let {
                    call.respond(HttpStatusCode.OK, it)
                } ?: call.respond(HttpStatusCode.NotFound, "Book not found")
            } ?: call.respond(HttpStatusCode.BadRequest, "Invalid book ID")
        }

        post {
            try {
                val request = call.receive<CreateBookRequest>()
                call.respond(HttpStatusCode.Created, libraryService.addBook(request.title, request.author))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request data")
            }
        }

        put("/{id}") {
            call.parameters["id"]?.toIntOrNull()?.let { id ->
                try {
                    val request = call.receive<UpdateBookRequest>()
                    libraryService.updateBook(id, request.title, request.author)?.let {
                        call.respond(HttpStatusCode.OK, it)
                    } ?: call.respond(HttpStatusCode.NotFound, "Book not found")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request data")
                }
            } ?: call.respond(HttpStatusCode.BadRequest, "Invalid book ID")
        }

        delete("/{id}") {
            call.parameters["id"]?.toIntOrNull()?.let { id ->
                if (libraryService.deleteBook(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Book not found")
                }
            } ?: call.respond(HttpStatusCode.BadRequest, "Invalid book ID")
        }
    }
}
