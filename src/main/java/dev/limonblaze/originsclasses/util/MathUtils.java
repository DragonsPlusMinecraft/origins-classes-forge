package dev.limonblaze.originsclasses.util;

import net.minecraft.util.RandomSource;

import java.util.Random;

public class MathUtils {

    public static int randomFloor(double d, RandomSource random) {
        int i = (int) d;
        return random.nextDouble() < d - i ? i + 1 : i;
    }
    
}
