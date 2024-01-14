package io.musicorum.api.realms.party.routes

import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.newClient
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.realms.party.schemas.PartyResponse
import io.musicorum.api.utils.generateNanoId
import io.musicorum.api.utils.getRequiredEnv

internal fun Route.createCreateRoute() {
    val redisEndpoint = Endpoint.from(getRequiredEnv(EnvironmentVariable.PartiesUrl))
    post {
        newClient(redisEndpoint).use {
            val roomId = generateNanoId(15)
            val userId = generateNanoId(10)
            it.sadd("users:$roomId", userId)
            it.set("owner:$roomId", userId)
            call.respond(PartyResponse(roomId, userId))
        }
    }
}