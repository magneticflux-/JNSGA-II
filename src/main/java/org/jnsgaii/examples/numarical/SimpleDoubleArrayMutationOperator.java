package org.jnsgaii.examples.numarical;

import org.apache.commons.math3.util.FastMath;
import org.jnsgaii.multiobjective.population.FrontedIndividual;
import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.operators.Operator;
import org.jnsgaii.population.Population;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by skaggsm on 12/27/15.
 */
@Deprecated
public class SimpleDoubleArrayMutationOperator implements Operator<double[]> {

    public static double clip(double lower, double toClip, double upper) {
        if (toClip > lower) {
            if (toClip < upper) {
                return toClip;
            } else return upper;
        } else return lower;
    }

    @Override
    public Population<double[]> apply(FrontedPopulation<double[]> population, Properties properties) {
        List<Individual<double[]>> individuals = new ArrayList<>(population.getPopulation().size());
        Random r = ThreadLocalRandom.current();

        double mutationStrengthMutationStrength = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.MUTATION_STRENGTH_MUTATION_STRENGTH);
        double mutationStrengthMutationProbability = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.MUTATION_STRENGTH_MUTATION_PROBABILITY);
        double mutationProbabilityMutationStrength = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.MUTATION_PROBABILITY_MUTATION_STRENGTH);
        double mutationProbabilityMutationProbability = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.MUTATION_PROBABILITY_MUTATION_PROBABILITY);

        for (FrontedIndividual<double[]> d : population.getPopulation()) {
            double[] newIndividual = Arrays.stream(d.getIndividual()).parallel().map(value -> (r.nextDouble() < d.getMutationProbability()) ? this.mutate(value, r, FastMath.pow(d.getMutationStrength(), 2)) : value).toArray();

            double mutationStrength = (r.nextDouble() < mutationStrengthMutationProbability) ? this.mutate(d.getMutationStrength(), r, mutationStrengthMutationStrength) : d.getMutationStrength();

            double mutationProbability = (r.nextDouble() < mutationProbabilityMutationProbability) ? this.mutate(d.getMutationProbability(), r, mutationProbabilityMutationStrength) : d.getMutationProbability();

            mutationStrength = clip(0, mutationStrength, Double.POSITIVE_INFINITY);
            mutationProbability = clip(0, mutationProbability, 1);

            individuals.add(new Individual<>(newIndividual, mutationStrength, mutationProbability, d.getCrossoverStrength(), d.getCrossoverProbability()));
        }
        return new Population<>(individuals);
    }

    private double mutate(double d, Random r, double range) {
        return (d + (r.nextDouble() * 2 * range)) - range;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.DoubleKey.DefaultDoubleKey.MUTATION_STRENGTH_MUTATION_STRENGTH, Key.DoubleKey.DefaultDoubleKey.MUTATION_STRENGTH_MUTATION_PROBABILITY,
                Key.DoubleKey.DefaultDoubleKey.MUTATION_PROBABILITY_MUTATION_STRENGTH, Key.DoubleKey.DefaultDoubleKey.MUTATION_PROBABILITY_MUTATION_PROBABILITY
        };
    }
}
