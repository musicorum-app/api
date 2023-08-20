package io.musicorum.api.utils

import com.aventrix.jnanoid.jnanoid.NanoIdUtils

fun generateNanoId(
    size: Int = 21
): String = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, size)