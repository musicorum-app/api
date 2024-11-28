package io.musicorum.api.realms.party.routes

import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.newClient
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.realms.party.schemas.PartyStatus
import io.musicorum.api.realms.party.schemas.PartyUser
import io.musicorum.api.security.AuthenticationMethod
import io.musicorum.api.utils.getRequiredEnv

internal fun Route.createStatsRoute() {
    val redisEndpoint = Endpoint.from(getRequiredEnv(EnvironmentVariable.PartiesUrl))
    authenticate(AuthenticationMethod.Super, AuthenticationMethod.Client) {
        get {
            val roomId = call.parameters["id"]
            val partyUsers = mutableListOf<PartyUser>()
            newClient(redisEndpoint).use {
                val users = it.smembers("users:$roomId")
                if (users.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                    return@use
                }
                val owner = it.get("owner:$roomId") ?: ""
                val ownerName = it.hget("sessions:$owner", "username") ?: ""
                users.forEach { uid ->
                    val username = it.hget("sessions:$uid", "username") ?: ""
                    partyUsers.add(PartyUser(id = uid, username = username))
                }
                call.respond(PartyStatus(owner = PartyUser(owner, ownerName), users = partyUsers))
            }
        }
    }
}