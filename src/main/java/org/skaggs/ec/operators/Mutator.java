package org.skaggs.ec.operators;

import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.*;
import org.skaggs.ec.util.Range;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * Created by skaggsm on 1/22/16.
 */
public abstract class Mutator<E> implements Function<Individual<E>, Individual<E>>, HasPropertyRequirements, LateUpdatingProperties, HasAspectRequirements {

    private double mutationProbabilityMutationProbability, mutationProbabilityMutationStrength, mutationStrengthMutationProbability, mutationStrengthMutationStrength;
    private int startIndex;

    public static double mutate(double d, Random r, double range) {
        return (d + (r.nextDouble() * 2 * range)) - range;
    }

    @Override
    public int requestAspectLocation(int startIndex) {
        this.startIndex = startIndex;
        return 2;
    }

    @Override
    public void updateProperties(Properties properties) {
        mutationStrengthMutationStrength = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.MUTATION_STRENGTH_MUTATION_STRENGTH);
        mutationStrengthMutationProbability = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.MUTATION_STRENGTH_MUTATION_PROBABILITY);
        mutationProbabilityMutationStrength = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.MUTATION_PROBABILITY_MUTATION_STRENGTH);
        mutationProbabilityMutationProbability = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.MUTATION_PROBABILITY_MUTATION_PROBABILITY);
    }

    @Override
    public Individual<E> apply(Individual<E> e) {
        Random r = ThreadLocalRandom.current();

        double[] newAspects = e.aspects.clone();

        if (r.nextDouble() < mutationStrengthMutationProbability)
            newAspects[startIndex] = Mutator.mutate(newAspects[startIndex], r, mutationStrengthMutationStrength);
        if (r.nextDouble() < mutationProbabilityMutationProbability)
            newAspects[startIndex + 1] = Mutator.mutate(newAspects[startIndex + 1], r, mutationProbabilityMutationStrength);

        newAspects[startIndex] = Range.clip(0, newAspects[startIndex], Double.POSITIVE_INFINITY);
        newAspects[startIndex + 1] = Range.clip(0, newAspects[startIndex + 1], 1);

        E individual = e.getIndividual();

        if (r.nextDouble() < e.getMutationProbability())
            individual = mutate(e.getIndividual(), e.getMutationStrength(), e.getMutationProbability());

        return new Individual<>(individual, newAspects);
    }

    protected abstract E mutate(E object, double mutationStrength, double mutationProbability);

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.DoubleKey.DefaultDoubleKey.MUTATION_STRENGTH_MUTATION_STRENGTH,
                Key.DoubleKey.DefaultDoubleKey.MUTATION_STRENGTH_MUTATION_PROBABILITY,
                Key.DoubleKey.DefaultDoubleKey.MUTATION_PROBABILITY_MUTATION_STRENGTH,
                Key.DoubleKey.DefaultDoubleKey.MUTATION_PROBABILITY_MUTATION_PROBABILITY
        };
    }
}
