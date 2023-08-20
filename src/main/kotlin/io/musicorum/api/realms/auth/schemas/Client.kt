package io.musicorum.api.realms.auth.schemas

import io.ktor.server.auth.*
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Client(
    val id: String,
    var name: String,
    var key: String,
    val createdAt: Instant,
    var updatedAt: Instant?
)

data class ClientPrincipal(val client: Client) : Principal