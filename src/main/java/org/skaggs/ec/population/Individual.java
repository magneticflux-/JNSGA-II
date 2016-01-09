package org.skaggs.ec.population;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Individual<E> {

    protected final E individual;
    protected final double mutationProbability = 0;
    protected final double crossoverProbability = 0;

    public Individual(E individual) {
        this.individual = individual;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Individual && ((Individual) obj).getIndividual().equals(this.getIndividual());
    }

    public E getIndividual() {
        return this.individual;
    }
}
