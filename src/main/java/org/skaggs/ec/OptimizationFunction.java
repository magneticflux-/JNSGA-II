package org.skaggs.ec;

import java.util.Comparator;

/**
 * Created by Mitchell on 11/25/2015.
 *
 * This class is a comparator on its own function. It decides which score bests which other score, which affects the algorithm's choices.
 * <p>
 * NOTE: An implementor must also implement the <code>equals</code> method.
 *
 * @see java.util.Comparator
 */
public interface OptimizationFunction<E> extends Comparator<Double> {

    double evaluate(E object);

    /**
     * @return the minimum value this function can return
     */
    double min();

    /**
     * @return the maximum value this function can return
     */
    double max();
}
