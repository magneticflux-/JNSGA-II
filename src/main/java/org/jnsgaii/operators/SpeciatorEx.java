package org.jnsgaii.operators;

import org.jnsgaii.properties.AspectUser;

import java.util.Collections;
import java.util.Set;

/**
 * Created by skaggsm on 10/27/16.
 * <p>
 * This class, rather than only providing tests regarding individuals, provides a set of species containing every individual in the population.
 * Replaces {@link Speciator}.
 */
public abstract class SpeciatorEx<E> extends AspectUser {
    public abstract Set<Species> getSpecies();

    public static class Species {
        private final long id;
        private final Set<Long> individualIDs;

        public Species(Set<Long> individualIDs, long id) {
            this.individualIDs = Collections.unmodifiableSet(individualIDs);
            this.id = id;
        }
    }
}
