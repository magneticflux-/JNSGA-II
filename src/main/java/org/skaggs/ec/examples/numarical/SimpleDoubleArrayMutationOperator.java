package org.skaggs.ec.examples.numarical;

import org.apache.commons.lang3.Range;
import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.multiobjective.population.FrontedPopulation;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.Population;
import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by skaggsm on 12/27/15.
 */
public class SimpleDoubleArrayMutationOperator implements Operator<double[]> {

    public static final Range<Double> probabilityRange = Range.between(0d, 1d);

    public static double clip(double lower, double toClip, double upper) {
        if (toClip > lower) {
            if (toClip < upper) {
                return toClip;
            } else return upper;
        } else return lower;
    }

    private double mutate(double d, Random r, double range) {
        return (d + (r.nextDouble() * 2 * range)) - range;
    }

    @Override
    public Population<double[]> apply(FrontedPopulation<double[]> population, Properties properties) {
        List<Individual<double[]>> individuals = new ArrayList<>(population.getPopulation().size());
        Random r = ThreadLocalRandom.current();

        double mutationStrengthMutationStrength = properties.getDouble(Key.DoubleKey.MUTATION_STRENGTH_MUTATION_STRENGTH);
        double mutationStrengthMutationProbability = properties.getDouble(Key.DoubleKey.MUTATION_STRENGTH_MUTATION_PROBABILITY);
        double mutationProbabilityMutationStrength = properties.getDouble(Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_STRENGTH);
        double mutationProbabilityMutationProbability = properties.getDouble(Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_PROBABILITY);

        for (FrontedIndividual<double[]> d : population.getPopulation()) {
            double[] newIndividual = Arrays.stream(d.getIndividual()).parallel().map(value -> (r.nextDouble() < d.mutationProbability) ? this.mutate(value, r, d.mutationStrength) : value).toArray();
            double mutationStrength = (r.nextDouble() < mutationStrengthMutationProbability) ? this.mutate(d.mutationStrength, r, mutationStrengthMutationStrength) : d.mutationStrength;
            double mutationProbability = (r.nextDouble() < mutationProbabilityMutationProbability) ? this.mutate(d.mutationProbability, r, mutationProbabilityMutationStrength) : d.mutationProbability;

            mutationStrength = clip(0, mutationStrength, Double.POSITIVE_INFINITY);
            mutationProbability = clip(0, mutationProbability, 1);

            individuals.add(new Individual<>(newIndividual, mutationStrength, mutationProbability, -1, -1));
        }
        return new Population<>(individuals);
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.DoubleKey.MUTATION_STRENGTH_MUTATION_STRENGTH, Key.DoubleKey.MUTATION_STRENGTH_MUTATION_PROBABILITY,
                Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_STRENGTH, Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_PROBABILITY
        };
    }
}
