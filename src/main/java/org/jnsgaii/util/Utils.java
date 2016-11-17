package org.jnsgaii.util;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by Mitchell Skaggs on 1/22/16.
 */
public class Utils {
    @SafeVarargs
    public static <T> T[] concat(T[]... arrays) {
        int length = Arrays.stream(arrays).mapToInt(value -> value.length).sum();
        @SuppressWarnings("unchecked")
        T[] newArray = (T[]) Array.newInstance(arrays[0].getClass().getComponentType(), length);
        int currentOffset = 0;
        for (T[] currentArray : arrays) {
            System.arraycopy(currentArray, 0, newArray, currentOffset, currentArray.length);
            currentOffset += currentArray.length;
        }
        return newArray;
    }
}
