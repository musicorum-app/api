package io.musicorum.api.realms.party.routes

import io.github.crackthecodeabhi.kreds.connection.Endpoint
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.utils.getRequiredEnv

fun Application.createPartyRoutes() {
    val redisEndpoint = Endpoint.from(getRequiredEnv(EnvironmentVariable.PartiesUrl))
    routing {
        route("party") {
            route("{id}") {
                createStatsRoute()

                route("/join") {
                    createJoinRoute()
                }
            }
            route("create") {
                createCreateRoute()
            }
        }
    }
}