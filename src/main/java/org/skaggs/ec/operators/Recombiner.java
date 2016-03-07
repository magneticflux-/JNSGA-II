package org.skaggs.ec.operators;

import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.HasPropertyRequirements;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.LateUpdatingProperties;
import org.skaggs.ec.properties.Properties;
import org.skaggs.ec.util.Range;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;


/**
 * Created by skaggsm on 1/22/16.
 */
public abstract class Recombiner<E> implements BiFunction<Individual<E>, Individual<E>, Individual<E>>, HasPropertyRequirements, LateUpdatingProperties {

    private double crossoverProbabilityMutationProbability, crossoverProbabilityMutationStrength, crossoverStrengthMutationProbability, crossoverStrengthMutationStrength;

    @Override
    public void updateProperties(Properties properties) {
        crossoverStrengthMutationStrength = properties.getDouble(Key.DoubleKey.CROSSOVER_STRENGTH_MUTATION_STRENGTH);
        crossoverStrengthMutationProbability = properties.getDouble(Key.DoubleKey.CROSSOVER_STRENGTH_MUTATION_PROBABILITY);
        crossoverProbabilityMutationStrength = properties.getDouble(Key.DoubleKey.CROSSOVER_PROBABILITY_MUTATION_STRENGTH);
        crossoverProbabilityMutationProbability = properties.getDouble(Key.DoubleKey.CROSSOVER_PROBABILITY_MUTATION_PROBABILITY);
    }

    @Override
    public Individual<E> apply(Individual<E> t, Individual<E> u) {
        Random r = ThreadLocalRandom.current();

        double[] newAspects = t.aspects.clone();

        boolean doCrossover = r.nextDouble() < t.getCrossoverProbability();

        newAspects[0] = (r.nextDouble() < crossoverStrengthMutationProbability) ? Mutator.mutate(newAspects[0], r, crossoverStrengthMutationStrength) : newAspects[0];
        newAspects[0] = Range.clip(0, newAspects[0], Double.POSITIVE_INFINITY);

        newAspects[1] = (r.nextDouble() < crossoverProbabilityMutationProbability) ? Mutator.mutate(newAspects[1], r, crossoverProbabilityMutationStrength) : newAspects[1];
        newAspects[1] = Range.clip(0, newAspects[1], 1);

        E individual = doCrossover ? crossover(t.getIndividual(), u.getIndividual(), newAspects[0], newAspects[1]) : t.getIndividual();

        return new Individual<>(individual, newAspects);
    }

    protected abstract E crossover(E parent1, E parent2, double crossoverStrength, double crossoverProbability);

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.DoubleKey.CROSSOVER_STRENGTH_MUTATION_STRENGTH,
                Key.DoubleKey.CROSSOVER_STRENGTH_MUTATION_PROBABILITY,
                Key.DoubleKey.CROSSOVER_PROBABILITY_MUTATION_STRENGTH,
                Key.DoubleKey.CROSSOVER_PROBABILITY_MUTATION_PROBABILITY
        };
    }
}
