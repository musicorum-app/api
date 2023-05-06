package io.musicorum.realms.resources

import io.musicorum.realms.resources.services.ResourcesService
import org.koin.dsl.module

val resourcesModule = module {
    single { ResourcesService() }
}