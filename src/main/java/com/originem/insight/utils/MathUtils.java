package com.originem.insight.utils;

import java.util.Random;

public class MathUtils {
    public static final Random RNG = new Random();
    public static final float FPI = (float) Math.PI;
    public static final float FTAU = (float) (Math.PI * 2.0);


    public static float getRandomNumberInRange(float min, float max) {
        return getRandomNumberInRange(RNG, min, max);
    }


    public static int getRandomNumberInRange(int min, int max) {
        return getRandomNumberInRange(RNG, min, max);
    }

    /**
     * Returns a random float within a given range.
     *
     * @param min The minimum value to select.
     * @param max The maximum value to select.
     * @return A random {@link Float} between {@code min} and {@code max}.
     */
    public static float getRandomNumberInRange(Random rng, float min, float max) {
        return rng.nextFloat() * (max - min) + min;
    }

    /**
     * Returns a random integer within a given range.
     *
     * @param min The minimum value to select.
     * @param max The maximum value to select (inclusive).
     * @return A random {@link Integer} between {@code min} and {@code max},
     * inclusive.
     */
    public static int getRandomNumberInRange(Random rng, int min, int max) {
        if (min >= max) {
            if (min == max) {
                return min;
            }

            return rng.nextInt((min - max) + 1) + max;
        }
        return rng.nextInt((max - min) + 1) + min;
    }

    public static double getAngle(double x1, double y1, double x2, double y2) {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

}
