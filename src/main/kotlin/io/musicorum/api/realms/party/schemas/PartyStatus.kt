package io.musicorum.api.realms.party.schemas

import kotlinx.serialization.Serializable

@Serializable
data class PartyStatus(
    val owner: PartyUser,
    val users: List<PartyUser>,
)
