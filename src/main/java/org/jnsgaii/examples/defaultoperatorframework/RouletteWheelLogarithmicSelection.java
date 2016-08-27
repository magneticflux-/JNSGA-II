package org.jnsgaii.examples.defaultoperatorframework;

import org.apache.commons.math3.util.FastMath;

/**
 * Created by Mitchell on 4/22/2016.
 */
public class RouletteWheelLogarithmicSelection<E> extends RouletteWheelSelection<E> {

    @Override
    protected double getWeight(int rank) {
        return FastMath.log(rank + 1);
    }
}
