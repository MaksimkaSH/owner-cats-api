package com.cats.service

import com.cats.model.Cat
import com.cats.model.Id
import com.cats.model.Owner
import com.cats.repository.CatsRepository
import com.cats.repository.OwnersRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import java.util.UUID

//уровень бизнес логики
class CatsService {
    suspend fun addNewCat(ctx: ApplicationCall) {
        val cat = ctx.receive<Cat>()
        val owner: Owner?
        if (cat.owner != null) {
            owner = OwnersRepository.getOwner(cat.owner)
            if (owner == null) {
                ctx.respond(HttpStatusCode.BadRequest, "Owner with id doesn't exist")
                return
            }
            val id = UUID.randomUUID()
            CatsRepository.addNewCat(
                Cat(
                    id = id,
                    name = cat.name,
                    color = cat.color,
                    owner = cat.owner,
                    birthday = cat.birthday
                )
            )
        } else {
            val id = UUID.randomUUID()
            CatsRepository.addNewCat(
                Cat(
                    id = id,
                    name = cat.name,
                    color = cat.color,
                    owner = null,
                    birthday = cat.birthday
                )
            )
        }
        ctx.respond(HttpStatusCode.OK, "Cat added successfully")
    }

    suspend fun getCatById(ctx: ApplicationCall) {
        val receive = ctx.receive<Id>()
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

    suspend fun deleteCatById(ctx: ApplicationCall){
        val receive = ctx.receive<Id>()
        if (receive.id == null) {
            ctx.respond(HttpStatusCode.BadRequest, "Incorrect id format")
            return
        }
        if(CatsRepository.deleteCatById(receive.id)){
            ctx.respond(HttpStatusCode.OK)
        }else{
            ctx.respond(HttpStatusCode.NotFound, "Cat with this id doesn't exist")
        }
    }

    suspend fun updateCatInformation(ctx: ApplicationCall){
        val cat = ctx.receive<Cat>()
        //мне как-то стало лень все обрабатывать
        if(CatsRepository.updateCatInformation(cat)){
            ctx.respond(HttpStatusCode.OK)
        }else{
            ctx.respond(HttpStatusCode.NotFound)
        }
    }
}