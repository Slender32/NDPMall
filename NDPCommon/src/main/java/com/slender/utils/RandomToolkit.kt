package com.slender.utils

import java.util.random.RandomGenerator

object RandomToolkit {
    private val generator: RandomGenerator = RandomGenerator.of("L64X128MixRandom")

    fun getRandomInt(min: Int, max: Int): Int
        = generator.nextInt(max - min + 1) + min

    val randomExpireTime: Long
        get() = getRandomInt(-TimeToolkit.Unit.MINUTE * 2, TimeToolkit.Unit.MINUTE * 2).toLong()
}
