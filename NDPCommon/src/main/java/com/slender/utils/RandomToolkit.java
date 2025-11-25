package com.slender.utils;

import java.util.random.RandomGenerator;

public final class RandomToolkit {
    private RandomToolkit() {}

    private static final RandomGenerator generator = RandomGenerator.of("L64X128MixRandom");

    public static int getRandomInt(final int min ,final int max) {
        return generator.nextInt(max-min+1)+min;
    }

    public static int getRandomExpireTime(){
        return getRandomInt(-TimeToolkit.Unit.MINUTE*2,TimeToolkit.Unit.MINUTE*2);
    }
}
