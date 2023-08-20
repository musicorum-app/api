package io.musicorum.api.realms.auth

import io.musicorum.api.realms.auth.services.ClientService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val authModule = module {
    singleOf(::ClientService)
}