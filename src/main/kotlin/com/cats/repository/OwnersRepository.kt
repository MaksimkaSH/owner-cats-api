package com.cats.repository

import com.cats.model.Owner
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object OwnersRepository : Table("owners") {
    private val id = OwnersRepository.uuid("id")
    private val name = OwnersRepository.varchar("name", 35)
    private val birthday = OwnersRepository.date("birthday")

    fun getOwner(id: UUID): Owner? {
        return try {
            transaction {
                val ownerModel = OwnersRepository.select { OwnersRepository.id.eq(id) }.single()
                Owner(
                    id = ownerModel[OwnersRepository.id],
                    name = ownerModel[name],
                    birthday = ownerModel[birthday]
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    fun addOwner(owner: Owner) {
        transaction {
            OwnersRepository.insert {
                it[id] = owner.id!!
                it[name] = owner.name
                it[birthday] = owner.birthday
            }
        }
    }

}