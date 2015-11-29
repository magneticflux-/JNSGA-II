package org.skaggs.ec.multiobjective.population;

import org.skaggs.ec.population.Population;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class FrontedPopulation<E> extends Population<E> {
    protected List<Front<E>> fronts;

    public FrontedPopulation(Population<E> population) {
        super(population);
        fronts = new ArrayList<>();
    }

}
