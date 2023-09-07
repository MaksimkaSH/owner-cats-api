package com.cats.service

import com.cats.model.Id
import com.cats.model.Owner
import com.cats.repository.CatsRepository
import com.cats.repository.OwnersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.UUID

class OwnersService {

    suspend fun getOwnerById(ctx: ApplicationCall) {
        val receive = ctx.receive<Id>()
        if (receive.id == null) {
            ctx.respond(HttpStatusCode.BadRequest)
            return
        }
        val owner = OwnersRepository.getOwner(receive.id)
        if (owner == null) {
            ctx.respond(HttpStatusCode.NotFound, "Can't found owner with this id")
            return
        }
        ctx.respond(HttpStatusCode.OK, owner)
    }

    suspend fun addNewOwner(ctx: ApplicationCall) {
        val receive = ctx.receive<Owner>()
        //лень обрабатывать
        val id = UUID.randomUUID()
        OwnersRepository.addOwner(
            Owner(
                id = id,
                name = receive.name,
                birthday = receive.birthday
            )
        )
        ctx.respond(HttpStatusCode.OK, "Ya ustal")
    }

    suspend fun getListOfCats(ctx: ApplicationCall) {
        val receive = ctx.receive<Id>()
        if (receive.id == null) {
            ctx.respond(HttpStatusCode.BadRequest)
            return
        }
        val cats = CatsRepository.getListOfCats(receive.id)
        if (cats.isEmpty()) {
            ctx.respond(HttpStatusCode.NotFound, " The owner with this id has no cats")
        } else {
            ctx.respond(HttpStatusCode.OK, cats)
        }

    }
}