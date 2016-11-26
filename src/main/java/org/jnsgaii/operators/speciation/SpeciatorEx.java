package org.jnsgaii.operators.speciation;

import org.jnsgaii.population.Population;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.AspectUser;
import org.jnsgaii.properties.LateUpdatingProperties;

import java.util.List;
import java.util.Set;

/**
 * Created by Mitchell Skaggs on 10/27/16.
 * <p>
 * This class, rather than only providing tests regarding individuals, provides a set of species containing every individual in the population.
 * <p>
 * Replaces {@link Speciator} and associated classes.
 */
public abstract class SpeciatorEx<E> extends AspectUser implements LateUpdatingProperties {

    /**
     * @param oldPopulation population from last generation
     * @param newPopulation the newly created, unspeciated offspring
     * @return a set of new species that are based on prior species, and including the new population
     */
    public abstract Set<Species> getSpecies(Population<E> oldPopulation, List<Individual<E>> newPopulation, long currentSpeciesID);
}
