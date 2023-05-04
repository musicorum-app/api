package io.musicorum.realms.auth

import io.musicorum.realms.auth.services.ClientService
import org.koin.dsl.module

val authModule = module {
    single { ClientService(get()) }
}