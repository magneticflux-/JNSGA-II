package org.jnsgaii.population;

import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Properties;

/**
 * Created by Mitchell Skaggs on 11/25/2015.
 */
public interface PopulationGenerator<E> extends HasPropertyRequirements {

    Population<E> generatePopulation(int populationSize, Properties properties);
}
