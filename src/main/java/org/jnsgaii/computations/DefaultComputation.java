package org.jnsgaii.computations;

import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Properties;

import java.util.List;

/**
 * Created by Mitchell on 6/5/2016.
 */
public abstract class DefaultComputation<E, F> implements Computation<E, F> {
    @Override
    public F[] compute(List<Individual<E>> individuals, Properties properties) {
        //noinspection unchecked
        return (F[]) individuals.parallelStream().map(individual -> computeIndividual(individual, properties)).toArray();
    }

    public abstract F computeIndividual(Individual<E> individual, Properties properties);
}
