package org.jnsgaii.population;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.util.FastMath;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Properties;

import java.util.*;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Population<E> {

    protected List<? extends Individual<E>> population;
    protected Map<Long, Integer> idToPopulationIndexMap = new IdentityHashMap<>();
    protected long currentID;

    protected Population() {
    }

    public Population(int size, PopulationGenerator<E> populationGenerator, Properties properties) {
        Pair<List<Individual<E>>, Long> pair = populationGenerator.generatePopulation(size, properties);
        this.population = pair.getLeft();
        this.currentID = pair.getRight();
        updateIDMap();
    }

    public Population(Population<E> population) {
        this.population = new ArrayList<>(population.population);
        this.currentID = population.currentID;
        this.idToPopulationIndexMap = new HashMap<>(population.idToPopulationIndexMap);
    }

    public Population(List<? extends Individual<E>> individuals, long currentID) {
        this.population = new ArrayList<>(individuals);
        this.currentID = currentID;
        updateIDMap();
    }

    public static <E> Population<E> merge(Population<E> population1, Population<E> population2) {
        ArrayList<Individual<E>> individuals = new ArrayList<>(population1.size() + population2.size());
        individuals.addAll(population1.getPopulation());
        individuals.addAll(population2.getPopulation());
        return new Population<>(individuals, FastMath.max(population1.getCurrentID(), population2.getCurrentID()));
    }

    public Map<Long, Integer> getIdToPopulationIndexMap() {
        return idToPopulationIndexMap;
    }

    protected void updateIDMap() {
        idToPopulationIndexMap.clear();
        for (int i = 0; i < population.size(); i++)
            idToPopulationIndexMap.put(population.get(i).id, i);
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
