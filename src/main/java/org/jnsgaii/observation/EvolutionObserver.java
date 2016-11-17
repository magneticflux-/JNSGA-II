package org.jnsgaii.observation;

import org.jnsgaii.population.PopulationData;

/**
 * Created by Mitchell Skaggs on 11/25/2015.
 */
@FunctionalInterface
public interface EvolutionObserver<E> {

    void update(PopulationData<E> populationData);
}
