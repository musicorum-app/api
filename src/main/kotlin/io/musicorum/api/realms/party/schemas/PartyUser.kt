package io.musicorum.api.realms.party.schemas

import kotlinx.serialization.Serializable

@Serializable
data class PartyUser(
    val id: String,
    val username: String
)
