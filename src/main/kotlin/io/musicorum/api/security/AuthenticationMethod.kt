package io.musicorum.api.security

import io.ktor.server.auth.*

object AuthenticationMethod {
    const val Super = "Super"
    const val Client = "Client"
}

class SuperPrincipal: Principal