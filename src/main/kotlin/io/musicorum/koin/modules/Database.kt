package io.musicorum.koin.modules

import io.musicorum.services.DatabaseService
import org.koin.dsl.module

val databaseModule = module {
    single<DatabaseService> { DatabaseService() }
}