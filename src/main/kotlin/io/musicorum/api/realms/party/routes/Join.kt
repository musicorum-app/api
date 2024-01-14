package io.musicorum.api.realms.party.routes

import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.github.crackthecodeabhi.kreds.connection.newClient
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.realms.party.schemas.PartyResponse
import io.musicorum.api.utils.generateNanoId
import io.musicorum.api.utils.getRequiredEnv

internal fun Route.createJoinRoute() {
    val redisEndpoint = Endpoint.from(getRequiredEnv(EnvironmentVariable.PartiesUrl))

    post {
        val roomId = call.parameters["id"]
        newClient(redisEndpoint).use {
            if (it.keys("users:$roomId").isEmpty()) {
                call.respond(HttpStatusCode.NotFound)
                return@use
            }
            val clientId = generateNanoId(10)
            it.sadd("users:$roomId", clientId)
            call.respond(PartyResponse(null, clientId))
        }
    }
}