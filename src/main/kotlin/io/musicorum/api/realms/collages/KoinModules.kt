package io.musicorum.api.realms.collages

import io.musicorum.api.realms.collages.repositories.CollagesRepository
import io.musicorum.api.realms.collages.services.CollagesService
import io.musicorum.api.realms.collages.services.WorkersService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val collagesModule = module(createdAtStart = true) {
    singleOf(::WorkersService)
    singleOf(::CollagesService)

    // repositories
    singleOf(::CollagesRepository)
}