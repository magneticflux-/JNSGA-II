package org.jnsgaii.population;

import org.apache.commons.lang3.tuple.Pair;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Properties;

import java.util.List;

/**
 * Created by Mitchell on 11/25/2015.
 */
public interface PopulationGenerator<E> extends HasPropertyRequirements {

    Pair<List<Individual<E>>, Long> generatePopulation(int num, Properties properties);
}
