package org.jnsgaii.population;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.util.FastMath;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Properties;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Population<E> {

    protected List<? extends Individual<E>> population;
    protected long currentID;

    protected Population() {
    }

    public Population(int size, PopulationGenerator<E> populationGenerator, Properties properties) {
        Pair<List<Individual<E>>, Long> pair = populationGenerator.generatePopulation(size, properties);
        this.population = pair.getLeft();
        this.currentID = pair.getRight();
    }

    public Population(Population<E> population) {
        this.population = new ArrayList<>(population.population);
        this.currentID = population.currentID;
    }

    public Population(List<? extends Individual<E>> individuals, long currentID) {
        this.population = new ArrayList<>(individuals);
        this.currentID = currentID;
    }

    public static <E> Population<E> merge(Population<E> population1, Population<E> population2) {
        ArrayList<Individual<E>> individuals = new ArrayList<>(population1.size() + population2.size());
        individuals.addAll(population1.getPopulation());
        individuals.addAll(population2.getPopulation());
        return new Population<>(individuals, FastMath.max(population1.getCurrentID(), population2.getCurrentID()));
    }

    public long getCurrentID() {
        return currentID;
    }

    public int size() {
        return this.population.size();
    }

    public List<? extends Individual<E>> getPopulation() {
        return this.population;
    }
}
