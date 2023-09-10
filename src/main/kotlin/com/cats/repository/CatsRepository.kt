package com.cats.repository

import com.cats.model.Cat

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.transactions.transaction
import org.postgresql.util.PSQLException

import java.util.NoSuchElementException
import java.util.UUID

object CatsRepository : Table("cats") {
    private val id = uuid("id")
    private val name = varchar("name", 15)
    private val color = varchar("color", 10)
    private val owner = uuid("owner").nullable()
    private val birthday = date("birthday")

    fun addNewCat(cat: Cat) {
        transaction {
            CatsRepository.insert {
                it[id] = cat.id!!
                it[name] = cat.name
                it[color] = cat.color
                it[owner] = cat.owner
                it[birthday] = cat.birthday
            }
        }
    }

    fun getCatById(id: UUID): Cat? {
        return try {
            transaction {
                val catModel = CatsRepository.select { CatsRepository.id.eq(id) }.single()
                Cat(
                    id = catModel[CatsRepository.id],
                    name = catModel[name],
                    birthday = catModel[birthday],
                    color = catModel[color],
                    owner = catModel[owner]
                )
            }
        } catch (exception: PSQLException) {
            null
        } catch (e: NoSuchElementException) {
            null
        }
    }

    fun deleteCatById(id: UUID): Boolean {
        var isDeleted = false
        transaction {
            isDeleted = CatsRepository.deleteWhere { CatsRepository.id.eq(id) } > 0
        }
        return isDeleted
    }

    fun updateCatInformation(cat: Cat): Cat? {
        var isUpdated = false
        transaction {
            isUpdated = CatsRepository.update({ CatsRepository.id.eq(cat.id!!) }) {
                it[name] = cat.name
                it[birthday] = cat.birthday
                it[color] = cat.color
                it[owner] = cat.owner
            } > 0
        }
        return if (isUpdated) {
            cat
        } else {
            null
        }
    }

    fun deleteOwnerById(id: UUID) {
        transaction {
            CatsRepository.update({ CatsRepository.owner.eq(id) }) {
                it[owner] = null
            }
        }
    }

    fun getListOfCats(id: UUID): List<Cat> {
        return transaction {
            val catModel = CatsRepository.select { owner.eq(id) }
            val cats = mutableListOf<Cat>()
            catModel.forEach { cat ->
                cats.add(
                    Cat(
                        id = cat[CatsRepository.id],
                        name = cat[name],
                        birthday = cat[birthday], color = cat[color], owner = cat[owner]
                    )
                )
            }
            cats
        }
    }
}
