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
public abstract class Crossoverer<E> extends LateUpdatingProperties implements BiFunction<Individual<E>, Individual<E>, Individual<E>>, HasPropertyRequirements {

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

        boolean doCrossover = r.nextDouble() < t.crossoverProbability;

        double mutationStrength = doCrossover ? (t.mutationStrength + u.mutationStrength) / 2 : t.mutationStrength;
        double mutationProbability = doCrossover ? (t.mutationProbability + u.mutationProbability) / 2 : t.mutationProbability;
        double crossoverStrength = Range.clip(0, (r.nextDouble() < crossoverStrengthMutationProbability) ? Mutator.mutate(t.mutationStrength, r, crossoverStrengthMutationStrength) : t.mutationStrength, Double.POSITIVE_INFINITY);
        double crossoverProbability = Range.clip(0, (r.nextDouble() < crossoverProbabilityMutationProbability) ? Mutator.mutate(t.mutationProbability, r, crossoverProbabilityMutationStrength) : t.mutationProbability, 1);

        E individual = doCrossover ? crossover(t.getIndividual(), u.getIndividual(), crossoverStrength, crossoverProbability) : t.getIndividual();

        return new Individual<>(individual, mutationStrength, mutationProbability, crossoverStrength, crossoverProbability);
    }

    protected abstract E crossover(E parent1, E parent2, double crossoverStrength, double crossoverProbability);

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.DoubleKey.INITIAL_CROSSOVER_STRENGTH,
                Key.DoubleKey.INITIAL_CROSSOVER_PROBABILITY,
                Key.DoubleKey.CROSSOVER_STRENGTH_MUTATION_STRENGTH,
                Key.DoubleKey.CROSSOVER_STRENGTH_MUTATION_PROBABILITY,
                Key.DoubleKey.CROSSOVER_PROBABILITY_MUTATION_STRENGTH,
                Key.DoubleKey.CROSSOVER_PROBABILITY_MUTATION_PROBABILITY
        };
    }
}
