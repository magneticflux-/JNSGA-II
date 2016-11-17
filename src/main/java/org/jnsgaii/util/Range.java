package org.jnsgaii.util;

/**
 * Created by Mitchell Skaggs on 1/22/16.
 */
public class Range {
    public static double clip(double min, double x, double max) {
        if (x > min) {
            if (x < max) {
                return x;
            } else return max;
        } else return min;
    }
}
