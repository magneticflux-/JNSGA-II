package org.skaggs.ec.population;

import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.HasPropertyRequirements;
import org.skaggs.ec.properties.Properties;

import java.util.List;

/**
 * Created by Mitchell on 11/25/2015.
 */
public interface PopulationGenerator<E> extends HasPropertyRequirements {

    List<Individual<E>> generatePopulation(int num, Properties properties);
}
