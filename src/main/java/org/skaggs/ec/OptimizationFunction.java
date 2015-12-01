package org.skaggs.ec;

import java.util.Comparator;

/**
 * Created by Mitchell on 11/25/2015.
 *
 * This class is a comparator on its own function. It decides which score bests which other score, which affects the algorithm.
 *
 * @see java.util.Comparator
 */
public interface OptimizationFunction<E> extends Comparator<Double> {

    double evaluate(E object);
}
