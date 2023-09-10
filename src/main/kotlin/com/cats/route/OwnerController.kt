package com.cats.route

import com.cats.service.OwnersService

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureOwnerRouting() {
    routing {
        route("/owner") {
            get {
                OwnersService().getOwnerById(this.context)
            }
            post {
                OwnersService().addNewOwner(this.context)
            }
            delete {
                OwnersService().deleteOwnerById(this.context)
            }
            route("/cats") {
                get {
                    OwnersService().getListOfCats(this.context)
                }
            }
        }
    }
}