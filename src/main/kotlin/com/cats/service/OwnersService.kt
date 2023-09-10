package com.cats.service

import com.cats.model.Id
import com.cats.model.Owner
import com.cats.repository.CatsRepository
import com.cats.repository.OwnersRepository

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

import java.util.UUID

class OwnersService {

    private suspend fun processId(ctx: ApplicationCall): UUID? {
        val receive: Id
        try {
            receive = ctx.receive<Id>()
        } catch (e: JsonConvertException) {
            ctx.respond(HttpStatusCode.BadRequest, "Bad request")
            return null
        }
        if (receive.id == null) {
            ctx.respond(HttpStatusCode.BadRequest, "Bad request")
            return null
        }
        return receive.id
    }

    suspend fun getOwnerById(ctx: ApplicationCall) {
        val id = processId(ctx) ?: return
        val owner = OwnersRepository.getOwner(id)
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
        val owner = Owner(
            id = id,
            name = receive.name,
            birthday = receive.birthday
        )
        OwnersRepository.addOwner(owner)
        ctx.respond(HttpStatusCode.OK, owner)
    }

    suspend fun getListOfCats(ctx: ApplicationCall) {
        val id = processId(ctx) ?: return
        val cats = CatsRepository.getListOfCats(id)
        if (cats.isEmpty()) {
            ctx.respond(HttpStatusCode.NotFound, "The owner with this id has no cats")
        } else {
            ctx.respond(HttpStatusCode.OK, cats)
        }
    }

    suspend fun deleteOwnerById(ctx: ApplicationCall) {
        val id = processId(ctx) ?: return
        if (OwnersRepository.deleteOwnerById(id)) {
            ctx.respond(HttpStatusCode.BadRequest, "Owner with this id doesn't exist")
            return
        }
        CatsRepository.deleteOwnerById(id)
        ctx.respond(HttpStatusCode.OK, "OK")
    }
}