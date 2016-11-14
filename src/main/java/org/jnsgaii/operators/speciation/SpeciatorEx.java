package org.jnsgaii.operators.speciation;

import org.jnsgaii.properties.AspectUser;

import java.util.Set;

/**
 * Created by skaggsm on 10/27/16.
 * <p>
 * This class, rather than only providing tests regarding individuals, provides a set of species containing every individual in the population.
 * Replaces {@link Speciator}.
 */
public abstract class SpeciatorEx<E> extends AspectUser {
    public abstract Set<Species> getSpecies();

}
