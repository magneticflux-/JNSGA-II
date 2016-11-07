package org.jnsgaii.operators;

import org.apache.commons.collections4.set.UnmodifiableSet;

/**
 * Created by skaggsm on 10/27/16.
 * <p>
 * This class, rather than only providing tests regarding individuals, provides a set of species containing every individual in the population
 */
public abstract class SpeciatorEx<E> extends Speciator<E> {
    public abstract UnmodifiableSet<Species<E>> getSpecies();

    public static class Species<E> {
        private final UnmodifiableSet<E> individuals;

        public Species(UnmodifiableSet<E> individuals) {
            this.individuals = individuals;
        }
    }
}
