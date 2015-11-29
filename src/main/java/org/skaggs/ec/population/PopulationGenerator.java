package org.skaggs.ec.population;

import java.util.List;

/**
 * Created by Mitchell on 11/25/2015.
 */
public interface PopulationGenerator<E> {

    List<Individual<E>> generatePopulation(int num);
}
