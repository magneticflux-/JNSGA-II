package org.skaggs.ec.examples;

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
                Key.DoubleKey.MUTATION_PROBABILITY,
                Key.DoubleKey.MUTATION_DISTRIBUTION_INDEX,
                Key.DoubleKey.CROSSOVER_PROBABILITY,
                Key.DoubleKey.CROSSOVER_DISTRIBUTION_INDEX
        };
    }
}
