package io.musicorum.realms.auth

import io.ktor.client.request.*
import io.ktor.server.testing.*
import io.musicorum.api.module
import kotlin.test.Test

class RoutesKtTest {

    @Test
    fun `test create client`() = testApplication {
        application {
            module()
        }
        client.post("/clients").apply {

        }
    }
}