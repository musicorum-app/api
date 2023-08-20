package io.musicorum.api.services

import io.musicorum.api.enums.EnvironmentVariable
import io.musicorum.api.utils.getRequiredEnv
import io.musicorum.lasfmclient.LastfmClient

private val lastfmKey = getRequiredEnv(EnvironmentVariable.LastfmApiKey)

val lastfmClient = LastfmClient(lastfmKey)