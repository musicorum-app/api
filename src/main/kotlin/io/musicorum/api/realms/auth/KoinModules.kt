package io.musicorum.api.realms.auth

import io.musicorum.api.realms.auth.services.ClientService
import org.koin.dsl.module

val authModule = module {
    single { ClientService(get()) }
}