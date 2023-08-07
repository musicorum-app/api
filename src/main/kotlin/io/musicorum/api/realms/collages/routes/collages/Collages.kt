package io.musicorum.api.realms.collages.routes.collages

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.api.realms.collages.services.CollagesService
import io.musicorum.api.realms.collages.themes.Theme
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Route.createCollageRoute() {
    val collagesService = inject<CollagesService>()

    post {
        val payload = call.receive<CollagePayloadReq>()

//        val data = collagesService.value.create(payload)

        call.respond(payload)
    }
}

@Serializable
data class CollagePayloadReq(
    val user: String,
    val theme: String,
    val options: Map<String, @Contextual Any>,
    @SerialName("hide_username")
    val hideUsername: Boolean,
)