package io.musicorum.api

import com.aventrix.jnanoid.jnanoid.NanoIdUtils

fun generateNanoId(
    size: Int = 21
) = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, size)