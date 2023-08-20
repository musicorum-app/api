package io.musicorum.api.security

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.realms.auth.schemas.ClientPrincipal
import io.musicorum.api.realms.auth.services.ClientService
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val clientService = inject<ClientService>().value

    val superPassword = System.getenv(EnvironmentVariable.SuperPassword)

    authentication {
        bearer(AuthenticationMethod.Client) {
            realm = "Access to application-only routes"
            authSchemes(AuthenticationMethod.Client)

            authenticate {
                val client = clientService.getByKey(it.token)
                if (client != null) ClientPrincipal(client) else null
            }
        }

        bearer(AuthenticationMethod.Super) {
            realm = "Access to internal-only routes"
            authSchemes(AuthenticationMethod.Super)

            authenticate {
                if (superPassword != null && it.token == superPassword) SuperPrincipal() else null
            }
        }
    }
}
