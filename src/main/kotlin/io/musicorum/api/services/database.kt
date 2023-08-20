package io.musicorum.api.services

import io.ktor.util.logging.*
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.utils.getEnv
import io.musicorum.api.utils.getRequiredEnv
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

val database = connect()

private fun connect(): Database {
    val databaseUri = getEnv(EnvironmentVariable.DatabaseUri)
    if (databaseUri !== null) {
        val user = getRequiredEnv(EnvironmentVariable.DatabaseUser)
        val pass = getRequiredEnv(EnvironmentVariable.DatabasePassword)
        return Database.connect(
            url = databaseUri,
            driver = "org.postgresql.Driver",
            user = user,
            password = pass
        )
    }

    KtorSimpleLogger("Database").warn("${EnvironmentVariable.DatabaseUri} environment variable not set. Using memory database. All data will be lost on restart!")

    return Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )
}

suspend fun <T> runQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }