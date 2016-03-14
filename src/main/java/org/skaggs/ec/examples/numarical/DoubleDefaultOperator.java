package org.skaggs.ec.examples.numarical;

import org.skaggs.ec.multiobjective.population.FrontedPopulation;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.Population;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

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
