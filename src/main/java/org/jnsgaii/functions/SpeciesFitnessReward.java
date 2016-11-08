package org.jnsgaii.functions;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jnsgaii.operators.Speciator;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

/**
 * Created by Mitchell on 5/25/2016.
 */
public class SpeciesFitnessReward<E> implements OptimizationFunction<E> {

    private final DefaultOptimizationFunction<E> wrapped;
    private final Speciator<E> speciator;

    private SpeciesFitnessReward(DefaultOptimizationFunction<E> wrapped, Speciator<E> speciator) {
        this.wrapped = wrapped;
        this.speciator = speciator;
    }

    public static <T> SpeciesFitnessReward<T> wrapOptimizationFunction(DefaultOptimizationFunction<T> wrapped, Speciator<T> speciator) {
        return new SpeciesFitnessReward<>(wrapped, speciator);
    }

    @Override
    public double[] evaluate(List<Individual<E>> individuals, HashMap<String, Object>[] computationResults, Properties properties) {
        return individuals.parallelStream() // For each individual
                .mapToDouble(individual -> IntStream.range(0, individuals.size()) // For each individual
                        .mapToObj((IntFunction<Pair<Individual<E>, HashMap<String, Object>>>) value -> new ImmutablePair<>(individuals.get(value), computationResults[value])) // Get it and it's computations
                        .filter(pair -> speciator.apply(individual, pair.getLeft())) // Speciate
                        .mapToDouble(pair -> wrapped.evaluateIndividual(pair.getLeft().getIndividual(), pair.getRight(), properties)) // Get scores
                        .average().orElseThrow(Error::new)).toArray();
    }

    @Override
    public double min(Properties properties) {
        return wrapped.min(properties);
    }

    @Override
    public double max(Properties properties) {
        return wrapped.max(properties);
    }

    @Override
    public boolean isDeterministic() {
        return false;
    }

    @Override
    public Comparator<Double> getComparator() {
        return wrapped.getComparator();
    }

    @Override
    public Key[] requestProperties() {
        return wrapped.requestProperties();
    }
}
