package org.jnsgaii.population;

import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Population<E> {

    protected List<? extends Individual<E>> population;

    protected Population() {
    }

    public Population(int size, PopulationGenerator<E> populationGenerator, Properties properties) {
        this.population = populationGenerator.generatePopulation(size, properties);
    }

    public Population(Population<E> population) {
        this.population = new ArrayList<>(population.population);
    }

    public Population(List<? extends Individual<E>> individuals) {
        this.population = new ArrayList<>(individuals);
    }

    public static <E> Population<E> merge(Population<E> population1, Population<E> population2) {
        ArrayList<Individual<E>> individuals = new ArrayList<>(population1.size() + population2.size());
        individuals.addAll(population1.getPopulation());
        individuals.addAll(population2.getPopulation());
        return new Population<>(individuals);
    }

    public int size() {
        return this.population.size();
    }

    public List<? extends Individual<E>> getPopulation() {
        return this.population;
    }
}
