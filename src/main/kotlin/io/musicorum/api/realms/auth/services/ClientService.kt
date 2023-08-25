package io.musicorum.api.realms.auth.services

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import io.musicorum.api.realms.auth.schemas.Client
import io.musicorum.api.services.database
import io.musicorum.api.services.runQuery
import kotlinx.datetime.toKotlinInstant
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

class ClientService {
    object Clients : Table("clients") {
        val id = varchar("id", 20)
        val name = varchar("name", 64)
        var key = varchar("key", 32)
        var createdAt = timestamp("created_at").clientDefault { Instant.now() }
        var updatedAt = timestamp("updated_at").nullable()
    }

    init {
        transaction(database) {
            SchemaUtils.create(Clients)
        }
    }

    suspend fun listAll() = runQuery {
        Clients.selectAll().map { mapResultToClient(it) }
    }

    suspend fun getByKey(key: String): Client? = runQuery {
        Clients.select { Clients.key eq key }.firstOrNull().let {
            return@runQuery mapNullableResultToClient(it)
        }
    }

    suspend fun createOne(name: String): Client = runQuery {
        val id = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 20)
        val key = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 32)

        val inserted = Clients.insert {
            it[Clients.id] = id
            it[Clients.name] = name
            it[Clients.key] = key
        }.resultedValues!!.first()

        return@runQuery mapResultToClient(inserted)
    }

    private fun mapResultToClient(row: ResultRow) = Client(
        row[Clients.id],
        row[Clients.name],
        row[Clients.key],
        row[Clients.createdAt].toKotlinInstant(),
        row[Clients.updatedAt]?.toKotlinInstant()
    )

    private fun mapNullableResultToClient(row: ResultRow?): Client? {
        return if (row != null) mapResultToClient(row) else null
    }





//    suspend fun getByKey(key: String): Application? {
//        println("key is "+ key)
//        if (key == "test") {
//            return Application("a", "Test app", "test", Clock.System.now(), null)
//        }
//        return null
//    }
}