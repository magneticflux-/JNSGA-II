package org.jnsgaii.operators.speciation;

import org.jnsgaii.population.Population;
import org.jnsgaii.population.individual.Individual;

import java.util.List;
import java.util.Set;

/**
 * Created by skaggsm on 11/17/16.
 */

public abstract class DistanceSpeciatorEx<E> extends SpeciatorEx<E> {

    protected abstract double getDistance(Individual<E> first, Individual<E> second);

    protected abstract double getMaxDistance(Individual<E> first, Individual<E> second);

    protected double getAverageDistance(List<Individual<E>> individuals, Individual<E> toCompare) {
        return individuals.stream().mapToDouble(individual -> getDistance(individual, toCompare)).average().orElseThrow(() -> new Error("Average not available!"));
    }

    protected double getAverageMaxDistance(List<Individual<E>> individuals, Individual<E> toCompare) {
        return individuals.stream().mapToDouble(individual -> getMaxDistance(individual, toCompare)).average().orElseThrow(() -> new Error("Average not available!"));
    }

    @Override
    public Set<Species> getSpecies(Set<Species> oldSpecies, Population<E> oldPopulation, Population<E> newPopulation) {
        return null;
    }
}
