package org.jnsgaii.examples.numarical;

import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.operators.Operator;
import org.jnsgaii.population.Population;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

/**
 * Created by Mitchell on 11/29/2015.
 */
public class DoubleDefaultOperator implements Operator<Double> {
    @Override
    public Population<Double> apply(FrontedPopulation<Double> population, Properties properties) {
        return null;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.DoubleKey.DefaultDoubleKey.INITIAL_MUTATION_PROBABILITY,
                Key.DoubleKey.DefaultDoubleKey.INITIAL_DOUBLE_MUTATION_DISTRIBUTION_INDEX,
                Key.DoubleKey.DefaultDoubleKey.INITIAL_CROSSOVER_PROBABILITY,
                Key.DoubleKey.DefaultDoubleKey.INITIAL_DOUBLE_CROSSOVER_DISTRIBUTION_INDEX
        };
    }
}
