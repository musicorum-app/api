package io.musicorum.api.realms.party.schemas

import kotlinx.serialization.Serializable

@Serializable
data class PartyResponse(
    val roomId: String?,
    val userSessionId: String,
)
