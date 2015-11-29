package org.skaggs.ec.operators;

import org.skaggs.ec.population.Individual;
import org.skaggs.ec.properties.HasPropertyRequirements;
import org.skaggs.ec.properties.Properties;

import java.util.List;

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
    List<Individual<E>> apply(List<Individual<E>> population, Properties properties);
}
