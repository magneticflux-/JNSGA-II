package org.skaggs.ec.population;

import org.skaggs.ec.multiobjective.population.FrontedPopulation;

/**
 * Created by Mitchell on 11/25/2015.
 * <p>
 * TODO Add stats about the population. Mean, min, max, standard deviation
 */
public class PopulationData<E> {
    private final FrontedPopulation<E> frontedPopulation;

    public PopulationData(FrontedPopulation<E> frontedPopulation) {
        this.frontedPopulation = frontedPopulation;
    }
}
