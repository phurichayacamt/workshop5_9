import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import repositories.BookRepository
import repositories.LendingRecordRepository
import routes.bookRoutes
import routes.lendingRoutes
import services.LibraryService

val bookRepository = BookRepository()
val lendingRecordRepository = LendingRecordRepository()
val libraryService = LibraryService(bookRepository, lendingRecordRepository)


fun main() {
    embeddedServer(Netty, port = 8081, host = "0.0.0.0", module = Application::module)
    //embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }


    routing {
        bookRoutes(libraryService)
        lendingRoutes(libraryService)


        get("/health") {
            call.respondText("OK")
        }
    }
} 