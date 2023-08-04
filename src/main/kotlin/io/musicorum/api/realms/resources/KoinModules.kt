package io.musicorum.api.realms.resources

import io.musicorum.api.realms.resources.services.ResourcesService
import org.koin.dsl.module

val resourcesModule = module {
    single { ResourcesService() }
}