package io.musicorum.api.realms.charts

import io.musicorum.api.realms.charts.repositories.ChartSnapshotRepository
import io.musicorum.api.realms.charts.repositories.ChartTrackRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val chartModules = module(createdAtStart = true) {
    singleOf(::ChartTrackRepository)
    singleOf(::ChartSnapshotRepository)
}