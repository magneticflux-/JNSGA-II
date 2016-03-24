package org.jnsgaii.population;

import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Properties;

import java.util.List;

/**
 * Created by Mitchell on 11/25/2015.
 */
public interface PopulationGenerator<E> extends HasPropertyRequirements {

    List<Individual<E>> generatePopulation(int num, Properties properties);
}
