package com.cats.service

import com.cats.model.Cat
import com.cats.model.Id
import com.cats.repository.CatsRepository
import com.cats.repository.OwnersRepository

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

import java.util.UUID

class CatsService {

    private suspend fun processCat(ctx: ApplicationCall) { //TODO сделать даже в отдельном файле

    }

    suspend fun addNewCat(ctx: ApplicationCall) {
        val cat: Cat
        try {
            cat = ctx.receive<Cat>()
        } catch (e: JsonConvertException) {
            ctx.respond(HttpStatusCode.BadRequest, "Bad request")
            return
        }
        if (cat.owner != null && OwnersRepository.getOwner(cat.owner) == null) {
            ctx.respond(HttpStatusCode.BadRequest, "Owner with id doesn't exist")
            return
        }
        val id = UUID.randomUUID()
        val kitten = Cat(
            id = id,
            name = cat.name,
            color = cat.color,
            owner = cat.owner,
            birthday = cat.birthday
        )
        CatsRepository.addNewCat(kitten)
        ctx.respond(HttpStatusCode.OK, kitten)
    }

    suspend fun getCatById(ctx: ApplicationCall) {
        val receive: Id
        try {
            receive = ctx.receive<Id>()
        } catch (e: JsonConvertException) {
            ctx.respond(HttpStatusCode.BadRequest, "Bad request")
            return
        }
        if (receive.id == null) {
            ctx.respond(HttpStatusCode.BadRequest, "Incorrect id format")
            return
        }
        val cat = CatsRepository.getCatById(receive.id)
        if (cat == null) {
            ctx.respond(HttpStatusCode.NotFound, "Cat with this id doesn't exist")
        } else {
            ctx.respond(HttpStatusCode.OK, cat)
        }
    }

    suspend fun deleteCatById(ctx: ApplicationCall) {
        val receive: Id
        try {
            receive = ctx.receive<Id>()
        } catch (e: JsonConvertException) {
            ctx.respond(HttpStatusCode.BadRequest, "Bad request")
            return
        }
        if (receive.id == null) {
            ctx.respond(HttpStatusCode.BadRequest, "Incorrect id format")
            return
        }
        if (CatsRepository.deleteCatById(receive.id)) {
            ctx.respond(HttpStatusCode.OK, "The cat was successfully removed")
        } else {
            ctx.respond(HttpStatusCode.NotFound, "Cat with this id doesn't exist")
        }
    }

    suspend fun updateCatInformation(ctx: ApplicationCall) {
        val cat: Cat
        //мне как-то стало лень все обрабатывать
        try {
            cat = ctx.receive<Cat>()
        } catch (e: JsonConvertException) {
            ctx.respond(HttpStatusCode.BadRequest, "Bad request")
            return
        }
        if (cat.id == null) {
            ctx.respond(HttpStatusCode.BadRequest, "Bad request")
            return
        }
        if (CatsRepository.updateCatInformation(cat) != null) {
            ctx.respond(HttpStatusCode.OK, cat)
        } else {
            ctx.respond(HttpStatusCode.NotFound, "Cat with this id doesn't exist")
        }
    }

}