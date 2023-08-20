package io.musicorum.api.realms.resources.exceptions

import io.ktor.client.statement.*

class ResourcesRequestException(
    val response: HttpResponse
): Exception()