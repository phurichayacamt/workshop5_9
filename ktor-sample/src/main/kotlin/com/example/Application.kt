package com.example

import com.example.com.example.routes.DatabaseFactory
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.routing.routing
import com.example.com.example.routes.libraryRoutes
import io.ktor.server.application.install

fun main() {
    DatabaseFactory.init()
    embeddedServer(Netty, port = 8080) {
        install(ContentNegotiation) {
            json()
        }
        routing {
            libraryRoutes()
        }
    }.start(wait = true)
}