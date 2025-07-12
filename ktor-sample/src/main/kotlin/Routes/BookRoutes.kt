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
            val books = libraryService.getAllBooks()
            call.respond(HttpStatusCode.OK, books)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid book ID")
                return@get
            }

            val book = libraryService.getBookById(id)
            if (book == null) {
                call.respond(HttpStatusCode.NotFound, "Book not found")
            } else {
                call.respond(HttpStatusCode.OK, book)
            }
        }


        post {
            try {
                val request = call.receive<CreateBookRequest>()
                val book = libraryService.addBook(request.title, request.author)
                call.respond(HttpStatusCode.Created, book)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request data")
            }
        }


        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid book ID")
                return@put
            }

            try {
                val request = call.receive<UpdateBookRequest>()
                val book = libraryService.updateBook(id, request.title, request.author)
                if (book == null) {
                    call.respond(HttpStatusCode.NotFound, "Book not found")
                } else {
                    call.respond(HttpStatusCode.OK, book)
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request data")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid book ID")
                return@delete
            }

            val success = libraryService.deleteBook(id)
            if (success) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Book not found")
            }
        }
    }
}