package org.skaggs.ec.examples.numarical;

import org.skaggs.ec.population.PopulationGenerator;
import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mitchell on 11/27/2015.
 */
public class DoublePopulationGenerator implements PopulationGenerator<Double> {

    @Override
    public List<Individual<Double>> generatePopulation(int num, Properties properties) {
        final double min = properties.getDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM);
        final double max = properties.getDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM);
        final double difference = max - min;

        if (max <= min)
            throw new IllegalArgumentException("Maximum must be greater than minimum!");

        Random r = new Random();
        List<Individual<Double>> pop = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            pop.add(new Individual<>((r.nextDouble() * difference) + min));
        }
        return pop;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM};
    }
}
