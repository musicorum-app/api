package io.musicorum.api.koin.modules

import io.musicorum.api.services.DatabaseService
import org.koin.dsl.module

val mainModule = module {
    single { DatabaseService() }
}