package org.jnsgaii.functions;

import java.util.Comparator;

/**
 * Created by Mitchell on 11/7/2016.
 */
public interface HasComparator<E> {
    Comparator<E> getComparator();
}
