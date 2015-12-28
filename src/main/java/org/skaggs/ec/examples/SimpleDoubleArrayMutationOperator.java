package org.skaggs.ec.examples;

import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.multiobjective.population.FrontedPopulation;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.Individual;
import org.skaggs.ec.population.Population;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by skaggsm on 12/27/15.
 */
public class SimpleDoubleArrayMutationOperator implements Operator<double[]> {
    @Override
    public Population<double[]> apply(FrontedPopulation<double[]> population, Properties properties) {
        List<Individual<double[]>> individuals = new ArrayList<>(population.getPopulation().size());
        Random r = new Random();
        double probability = properties.getDouble(Key.DoubleKey.MUTATION_PROBABILITY);
        double range = properties.getDouble(Key.DoubleKey.DOUBLE_MUTATION_RANGE);

        for (FrontedIndividual<double[]> d : population.getPopulation()) {
            double[] newIndividual = Arrays.stream(d.getIndividual()).map(value -> (r.nextDouble() < probability) ? this.mutate(value, r, range) : value).toArray();
            individuals.add(new Individual<>(newIndividual));
        }

        return new Population<>(individuals);
    }

    private double mutate(double d, Random r, double range) {
        return (d + (r.nextDouble() * 2 * range)) - range;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{Key.DoubleKey.DOUBLE_MUTATION_RANGE};
    }
}
