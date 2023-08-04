package io.musicorum.api.realms.collages

import io.musicorum.api.realms.collages.services.CollagesService
import io.musicorum.api.realms.collages.services.WorkersService
import org.koin.dsl.module

val collagesModule = module {
    single { WorkersService() }
    single { CollagesService(get(), get()) }
}