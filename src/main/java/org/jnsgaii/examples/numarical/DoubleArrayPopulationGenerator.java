package org.jnsgaii.examples.numarical;

import org.jnsgaii.population.Population;
import org.jnsgaii.population.PopulationGenerator;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;
import org.jnsgaii.properties.Requirement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by skaggsm on 12/27/15.
 */
public class DoubleArrayPopulationGenerator implements PopulationGenerator<double[]> {
    @Override
    public Population<double[]> generatePopulation(int num, Properties properties) {
        Random r = ThreadLocalRandom.current();
        double min = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM);
        double max = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM);
        int length = properties.getInt(Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH);

        double[] initialAspects = (double[]) properties.getValue(Key.DoubleKey.DefaultDoubleKey.INITIAL_ASPECT_ARRAY);

        List<Individual<double[]>> individuals = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            individuals.add(new Individual<>(this.getIndividual(r, length, min, max), initialAspects.clone(), -1));
        }
        return new Population<>(individuals, -1L);
    }

    private double[] getIndividual(Random r, int length, double min, double max) {
        double[] val = new double[length];
        for (int i = 0; i < val.length; i++) {
            val[i] = this.getRandomDouble(r, min, max);
        }
        return val;
    }

    private double getRandomDouble(Random r, double min, double max) {
        return (r.nextDouble() * (max - min)) + min;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH
        };
    }

    @Override
    public Requirement[] requestDetailedRequirements() {
        return new Requirement[]{
                new Requirement() {
                    @Override
                    public String describe() {
                        return "DefaultDoubleKey \"INITIAL_ASPECT_ARRAY\" must be of type\"double[]\"";
                    }

                    @Override
                    public boolean test(Properties properties) {
                        return properties.getValue(Key.DoubleKey.DefaultDoubleKey.INITIAL_ASPECT_ARRAY) instanceof double[];
                    }
                }
        };
    }
}
