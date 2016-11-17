package org.jnsgaii.multiobjective.population;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Mitchell Skaggs on 6/9/2016.
 */
public class SortedArrayList<E extends Comparable<? super E>> extends ArrayList<E> {
    @Override
    public boolean add(E e) {
        boolean result = super.add(e);
        Collections.sort(this);
        return result;
    }

    @Override
    public E set(int index, E element) {
        E result = super.set(index, element);
        Collections.sort(this);
        return result;
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }
}
