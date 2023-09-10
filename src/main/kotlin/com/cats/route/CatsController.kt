package com.cats.route

import com.cats.service.CatsService

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureCatsRouting() {
    routing {
        route("/cat") {
            get {
                CatsService().getCatById(this.context)
            }
            post {
                CatsService().addNewCat(this.context)
            }
            put {
                CatsService().updateCatInformation(this.context)
            }
            delete {
                CatsService().deleteCatById(this.context)
            }
        }

    }
}