package org.skaggs.ec.population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Population<E> {
    public List<? extends Individual<E>> population;

    protected Population() {
    }

    public Population(int size, PopulationGenerator<E> populationGenerator) {
        population = populationGenerator.generatePopulation(size);
    }

    public Population(Population<E> population) {
        this.population = new ArrayList<>(population.population);
    }

    public Population(List<? extends Individual<E>> individuals) {
        population = new ArrayList<>(individuals);
    }

    public static <E> Population<E> merge(Population<E> population1, Population<E> population2) {
        return null;
    }

    public List<? extends Individual<E>> getPopulation() {
        return Collections.unmodifiableList(population);
    }
}
