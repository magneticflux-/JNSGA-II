package org.jnsgaii.computations;

import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Properties;

import java.util.List;

/**
 * Created by Mitchell on 6/4/2016.
 * <p>
 * Computations are designed to be long-running processes, while functions are near-instant and utilize computation results
 */
public interface Computation<E, F> {
    F[] compute(List<Individual<E>> individuals, Properties properties);

    String getComputationID();

    boolean isDeterministic();
}
