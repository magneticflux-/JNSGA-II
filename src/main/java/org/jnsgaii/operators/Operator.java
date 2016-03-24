package org.jnsgaii.operators;

import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.population.Population;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Properties;

/**
 * Created by Mitchell on 11/25/2015.
 */
public interface Operator<E> extends HasPropertyRequirements {

    /**
     * This method applies the operation to the entire population and returns a new collection of individuals.
     *
     * @param population the population to be operated on
     * @return a new population with the changes applied
     */
    Population<E> apply(FrontedPopulation<E> population, Properties properties);
}
