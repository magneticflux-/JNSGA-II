package org.skaggs.ec.population;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Individual<E> {

    protected final E individual;

    public Individual(E individual) {
        this.individual = individual;
    }

    public E getIndividual() {
        return this.individual;
    }
}
