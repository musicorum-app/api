package io.musicorum.api.realms.collages.routes.collages

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.musicorum.api.realms.collages.repositories.CollagesRepository
import io.musicorum.api.realms.collages.services.CollagesService
import io.musicorum.api.realms.collages.themes.Theme
import io.musicorum.api.realms.collages.themes.serialization.ThemeData
import io.musicorum.api.realms.collages.themes.serialization.ThemeDataSerializer
import io.musicorum.api.security.AuthenticationMethod
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

fun Route.createCollageRoute() {
    val collagesService = inject<CollagesService>()
    val collagesRepository = inject<CollagesRepository>()

    authenticate(AuthenticationMethod.Super, AuthenticationMethod.Client) {
        post {
            val payload = call.receive<CollagePayloadReq>()

            val data = collagesService.value.create(
                Theme.CollagePayload(
                    user = payload.user,
                    theme = payload.theme,
                    hideUsername = payload.hideUsername
                )
            )

            call.respond(data)
        }

        get {
            call.respond(collagesRepository.value.listAll())
        }
    }
}

@Serializable
data class CollagePayloadReq(
    val user: String,
    @Serializable(with = ThemeDataSerializer::class)
    val theme: ThemeData,
    @SerialName("hide_username")
    val hideUsername: Boolean,
)