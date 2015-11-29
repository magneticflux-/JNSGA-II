package org.skaggs.ec.example;

import org.apache.commons.math3.util.FastMath;
import org.skaggs.ec.multiobjective.population.FrontedPopulation;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.Population;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.util.Random;

/**
 * Created by Mitchell on 11/27/2015.
 */
public class DoubleCrossoverOperator implements Operator<Double> {

    private final Random random;
    private final double n;

    public DoubleCrossoverOperator(Random random, double n) {
        this.random = random;
        this.n = n;
    }

    private double[] crossover(double p1, double p2) {
        double u = random.nextDouble();
        double betaPrime = 0;
        while (probabilityFunctionIntegral(betaPrime) < u) // Computes the inverse of the function
            betaPrime += .01;

        return new double[]{p1 + p2 - betaPrime * FastMath.abs(p2 - p1), p1 + p2 + betaPrime * FastMath.abs(p2 - p1)};
    }

    private double probabilityFunctionIntegral(double x) {
        if (x <= 1)
            return FastMath.pow(x, n + 1) / 2;
        else
            return -FastMath.pow(x, -n - 1) / 2 + 1;
    }

    @Override
    public Population<Double> apply(FrontedPopulation<Double> population, Properties properties) {
        return null;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{Key.DoubleKey.CROSSOVER_DISTRIBUTION_INDEX, Key.DoubleKey.CROSSOVER_PROBABILITY};
    }
}
