package io.musicorum.api.utils

fun getRequiredEnv(name: String): String {
    return System.getenv(name) ?: throw Exception("$name Environment variable is null")
}

fun getEnv(name: String): String? {
    return System.getenv(name)
}