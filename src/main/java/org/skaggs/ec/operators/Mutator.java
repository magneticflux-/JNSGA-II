package org.skaggs.ec.operators;

import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.HasPropertyRequirements;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.LateUpdatingProperties;
import org.skaggs.ec.properties.Properties;
import org.skaggs.ec.util.Range;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * Created by skaggsm on 1/22/16.
 */
public abstract class Mutator<E> implements Function<Individual<E>, Individual<E>>, HasPropertyRequirements, LateUpdatingProperties {

    private double mutationProbabilityMutationProbability, mutationProbabilityMutationStrength, mutationStrengthMutationProbability, mutationStrengthMutationStrength;

    public static double mutate(double d, Random r, double range) {
        return (d + (r.nextDouble() * 2 * range)) - range;
    }

    @Override
    public void updateProperties(Properties properties) {
        mutationStrengthMutationStrength = properties.getDouble(Key.DoubleKey.MUTATION_STRENGTH_MUTATION_STRENGTH);
        mutationStrengthMutationProbability = properties.getDouble(Key.DoubleKey.MUTATION_STRENGTH_MUTATION_PROBABILITY);
        mutationProbabilityMutationStrength = properties.getDouble(Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_STRENGTH);
        mutationProbabilityMutationProbability = properties.getDouble(Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_PROBABILITY);
    }

    @Override
    public Individual<E> apply(Individual<E> e) {
        Random r = ThreadLocalRandom.current();

        double mutationStrength = (r.nextDouble() < mutationStrengthMutationProbability) ? mutate(e.mutationStrength, r, mutationStrengthMutationStrength) : e.mutationStrength;
        mutationStrength = Range.clip(0, mutationStrength, Double.POSITIVE_INFINITY);
        double mutationProbability = (r.nextDouble() < mutationProbabilityMutationProbability) ? mutate(e.mutationProbability, r, mutationProbabilityMutationStrength) : e.mutationProbability;
        mutationProbability = Range.clip(0, mutationProbability, 1);

        E individual = (r.nextDouble() < e.mutationProbability) ? mutate(e.getIndividual(), e.mutationStrength, e.mutationProbability) : e.getIndividual();

        return new Individual<>(individual, mutationStrength, mutationProbability, e.crossoverStrength, e.crossoverProbability);
    }

    protected abstract E mutate(E object, double mutationStrength, double mutationProbability);

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.DoubleKey.INITIAL_MUTATION_STRENGTH,
                Key.DoubleKey.INITIAL_MUTATION_PROBABILITY,
                Key.DoubleKey.MUTATION_STRENGTH_MUTATION_STRENGTH,
                Key.DoubleKey.MUTATION_STRENGTH_MUTATION_PROBABILITY,
                Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_STRENGTH,
                Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_PROBABILITY
        };
    }
}
