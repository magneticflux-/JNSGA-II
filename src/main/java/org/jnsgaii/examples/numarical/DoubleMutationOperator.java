package org.jnsgaii.examples.numarical;

import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.operators.Operator;
import org.jnsgaii.population.Population;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import java.util.Random;

/**
 * Created by Mitchell Skaggs on 11/27/2015.
 */
public class DoubleMutationOperator implements Operator<Double> {

    private final ThreadLocal<Random> random;

    public DoubleMutationOperator() {
        this.random = ThreadLocal.withInitial(Random::new);
    }

    private Individual<Double> mutate(Individual<Double> d) {
        return new Individual<>((d.getIndividual() + this.random.get().nextDouble()) - .5);
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{Key.DoubleKey.DefaultDoubleKey.INITIAL_MUTATION_PROBABILITY, Key.DoubleKey.DefaultDoubleKey.INITIAL_DOUBLE_MUTATION_DISTRIBUTION_INDEX};
    }

    @Override
    public Population<Double> apply(FrontedPopulation<Double> population, Properties properties) {
        return null;
    }
}
