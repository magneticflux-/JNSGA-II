package org.jnsgaii;

import org.jnsgaii.population.PopulationData;

/**
 * Created by Mitchell on 11/25/2015.
 */
@FunctionalInterface
public interface EvolutionObserver<E> {

    void update(PopulationData<E> populationData);
}
