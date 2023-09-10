package com.cats

import com.cats.plugins.*
import com.cats.route.configureCatsRouting
import com.cats.route.configureOwnerRouting
import io.ktor.http.*

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import org.jetbrains.exposed.sql.Database

fun main() {
    Database.connect("jdbc:postgresql://localhost:5432/cats-lab", driver = "org.postgresql.Driver", password = "", user = "postgres")

    embeddedServer(CIO, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    println(DEFAULT_PORT)
    configureSerialization()
    configureRouting()
    configureCatsRouting()
    configureOwnerRouting()
}
