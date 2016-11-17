package org.jnsgaii.operators;

import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.population.Population;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Properties;

/**
 * Created by Mitchell Skaggs on 11/25/2015.
 */
public interface Operator<E> extends HasPropertyRequirements {

    /**
     * This method applies the operation to the entire population and returns a new collection of individuals.
     *
     * @param population the population to be operated on, speciated
     * @return a new population including both the old population and new individuals, speciated
     */
    Population<E> apply(FrontedPopulation<E> population, Properties properties);
}
