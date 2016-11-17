package org.jnsgaii.population;

import org.jnsgaii.operators.speciation.Species;
import org.jnsgaii.population.individual.Individual;

import java.util.*;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Population<E> {

    protected List<? extends Individual<E>> population;
    protected Map<Long, Integer> idToPopulationIndexMap = new IdentityHashMap<>();
    protected long currentIndividualID, currentSpeciesID;
    protected Set<Species> species;

    protected Population() {
    }

    public Population(Population<E> population) {
        this.population = new ArrayList<>(population.population);
        this.currentIndividualID = population.currentIndividualID;
        this.currentSpeciesID = population.currentSpeciesID;
        this.idToPopulationIndexMap = new HashMap<>(population.idToPopulationIndexMap);
        this.species = population.species;
    }

    public Population(List<? extends Individual<E>> individuals, Set<Species> species, long currentSpeciesID, long currentIndividualID) {
        this.population = new ArrayList<>(individuals);
        this.currentIndividualID = currentIndividualID;
        this.currentSpeciesID = currentSpeciesID;
        updateIDMap();
    }

    public Set<Species> getSpecies() {
        return species;
    }

    public long getCurrentSpeciesID() {
        return currentSpeciesID;
    }

    public Map<Long, Integer> getIdToPopulationIndexMap() {
        return idToPopulationIndexMap;
    }

    protected void updateIDMap() {
        idToPopulationIndexMap.clear();
        for (int i = 0; i < population.size(); i++)
            idToPopulationIndexMap.put(population.get(i).id, i);
    }

    public long getCurrentIndividualID() {
        return currentIndividualID;
    }

    public int size() {
        return this.population.size();
    }

    public List<? extends Individual<E>> getPopulation() {
        return this.population;
    }
}
