package io.musicorum.realms.collages

import io.musicorum.realms.collages.services.WorkersService
import org.koin.dsl.module

val collagesModule = module {
    single { WorkersService() }
}