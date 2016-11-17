package org.jnsgaii.examples.numarical;

import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.operators.Operator;
import org.jnsgaii.population.Population;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

/**
 * Created by Mitchell Skaggs on 12/27/15.
 */
public class PolynomialDoubleMutationOperator implements Operator<Double> {
    @Override
    public Population<Double> apply(FrontedPopulation<Double> population, Properties properties) {
        return null;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[0];
    }
}
