package org.jnsgaii.examples.numarical;

import org.jnsgaii.population.Population;
import org.jnsgaii.population.PopulationGenerator;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mitchell on 11/27/2015.
 */
public class DoublePopulationGenerator implements PopulationGenerator<Double> {

    @Override
    public Population<Double> generatePopulation(int num, Properties properties) {
        final double min = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM);
        final double max = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM);
        final double difference = max - min;

        if (max <= min)
            throw new IllegalArgumentException("Maximum must be greater than minimum!");

        Random r = new Random();
        List<Individual<Double>> pop = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            pop.add(new Individual<>((r.nextDouble() * difference) + min));
        }
        return new Population<>(pop, -1L);
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM};
    }
}
