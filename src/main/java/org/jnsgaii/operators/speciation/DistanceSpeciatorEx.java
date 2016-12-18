package org.jnsgaii.operators.speciation;

import org.jnsgaii.population.Population;
import org.jnsgaii.population.individual.Individual;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mitchell Skaggs on 11/17/16.
 */

public abstract class DistanceSpeciatorEx<E> extends SpeciatorEx<E> {

    @Override
    public Set<Species> getSpecies(Population<E> oldPopulation, List<Individual<E>> newPopulation, long currentSpeciesID) {
        Set<Species> newSpecies = oldPopulation.getSpecies().stream().map(species -> new Species(species).thaw()).collect(Collectors.toSet());
        //Set<Long> allUsedIndividualIDs = new HashSet<>();

        Map<Long, Integer> idToNewPopulationIndexMap = new HashMap<>(newPopulation.size(), 1f);
        for (int i = 0; i < newPopulation.size(); i++)
            idToNewPopulationIndexMap.put(newPopulation.get(i).id, i);

        for (Individual<E> individual : newPopulation) {
            //allUsedIndividualIDs.add(individual.id);
            double smallestDistance = Double.MAX_VALUE;
            Species smallestDistanceSpecies = null;
            for (Species species : newSpecies) {
                List<Individual<E>> speciesIndividuals = species.getIndividualIDs().stream()
                        .map(id -> {
                            if (oldPopulation.getIdToPopulationIndexMap().containsKey(id))
                                return oldPopulation.getIndividualByID(id);
                            else if (idToNewPopulationIndexMap.containsKey(id))
                                return newPopulation.get(idToNewPopulationIndexMap.get(id));
                            else
                                throw new Error("Individual's ID (" + id + ") was found in neither new nor old populations!");
                        })
                        .collect(Collectors.toList());

                double distance = getAverageDistance(speciesIndividuals, individual);
                double maxDistance = getAverageMaxDistance(speciesIndividuals, individual);

                if (distance < maxDistance && distance < smallestDistance) {
                    smallestDistance = distance;
                    smallestDistanceSpecies = species;
                }
            }

            if (smallestDistanceSpecies != null) {
                smallestDistanceSpecies.getIndividualIDs().add(individual.id);
            } else {
                Species uniqueSpecies = new Species(new HashSet<>(Collections.singleton(individual.id)), currentSpeciesID++, false);
                newSpecies.add(uniqueSpecies);
                //System.out.println("Created new species " + uniqueSpecies);
            }
        }

        newSpecies.forEach(Species::freeze);

        //System.out.println("Included " + newSpecies.stream().mapToInt(s -> s.getIndividualIDs().size()).sum() + " individuals in " + newSpecies.size() + " species");

        //HashSet<Long> allIncludedIDs = new HashSet<>();
        //newSpecies.stream().map(Species::getIndividualIDs).forEach(allIncludedIDs::addAll);
        //System.out.println("Included: " + allIncludedIDs.size() + " " + allIncludedIDs);
        //System.out.println("Possible: " + allUsedIndividualIDs.size() + " " + allUsedIndividualIDs);

        return newSpecies;
    }

    protected double getAverageDistance(Collection<Individual<E>> individuals, Individual<E> toCompare) {
        return individuals.stream().mapToDouble(individual -> getDistance(individual, toCompare)).average().orElseThrow(() -> new Error("Average not available!"));
    }

    protected double getAverageMaxDistance(Collection<Individual<E>> individuals, Individual<E> toCompare) {
        return individuals.stream().mapToDouble(individual -> getMaxDistance(individual, toCompare)).average().orElseThrow(() -> new Error("Average not available!"));
    }

    protected abstract double getDistance(Individual<E> first, Individual<E> second);

    protected abstract double getMaxDistance(Individual<E> first, Individual<E> second);
}
