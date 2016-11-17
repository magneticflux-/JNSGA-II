package org.jnsgaii.examples.defaultoperatorframework;

/**
 * Created by Mitchell Skaggs on 2/10/2016.
 */
public class RouletteWheelLinearSelection<E> extends RouletteWheelSelection<E> {

    @Override
    protected double getWeight(int rank) {
        return rank;
    }
}
