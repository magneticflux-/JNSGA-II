package org.skaggs.ec.examples;

import org.apache.commons.math3.util.FastMath;
import org.skaggs.ec.OptimizationFunction;

/**
 * Created by Mitchell on 11/30/2015.
 */
class DoubleOptimizationFunction1 implements OptimizationFunction<Double> {
    @Override
    public double evaluate(Double object) {
        return FastMath.pow(object - 1, 2);
    }

    @Override
    public int compare(Double o1, Double o2) {
        return (int) FastMath.round(FastMath.signum(o2 - o1)); // Lower numbers are better. If o1 is lower than o2, +1 is returned.
    }
}

class DoubleOptimizationFunction2 implements OptimizationFunction<Double> {
    @Override
    public double evaluate(Double object) {
        return -FastMath.pow(object + 1, 2);
    }

    @Override
    public int compare(Double o1, Double o2) {
        return (int) FastMath.round(FastMath.signum(o1 - o2)); // Higher numbers are better. If o1 is higher than o2, +1 is returned.
    }
}
