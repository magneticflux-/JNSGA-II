package org.jnsgaii.operators.speciation;

import org.jnsgaii.population.Population;
import org.jnsgaii.properties.AspectUser;

import java.util.Set;

/**
 * Created by Mitchell Skaggs on 10/27/16.
 * <p>
 * This class, rather than only providing tests regarding individuals, provides a set of species containing every individual in the population.
 * Replaces {@link Speciator}.
 */
public abstract class SpeciatorEx<E> extends AspectUser {
    /**
     * @param oldSpecies    species from last generation
     * @param oldPopulation population from last generation
     * @param newPopulation the newly created, unspeciated offspring
     * @return a set of new species that are based on prior species, and including the new population
     */
    public abstract Set<Species> getSpecies(Set<Species> oldSpecies, Population<E> oldPopulation, Population<E> newPopulation);
}
