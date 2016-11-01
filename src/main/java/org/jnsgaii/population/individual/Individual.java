package org.jnsgaii.population.individual;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Individual<E> extends PopulationMember {

    protected final E individual;

    @Deprecated
    public Individual(E individual) {
        this(individual, new double[0], -1);
    }

    public Individual(E individual, double[] aspects, long id) {
        super(aspects, id);
        this.individual = individual;
    }

    public Individual(Individual<E> individual) {
        super(individual);
        this.individual = individual.individual;
    }

    @Deprecated
    public Individual(E individual, double mutationStrength, double mutationProbability, double crossoverStrength, double crossoverProbability) {
        super(new double[0], -1);
        throw new UnsupportedOperationException("Do not use mutation/crossover. Aspects only");
        //super(mutationStrength, mutationProbability, crossoverStrength, crossoverProbability);
        //this.individual = individual;
    }

    @SuppressWarnings("AssignmentToNull")
    protected Individual() {
        super();
        individual = null;
    }

    public E getIndividual() {
        return this.individual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Individual<?> that = (Individual<?>) o;

        return individual != null ? individual.equals(that.individual) : that.individual == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (individual != null ? individual.hashCode() : 0);
        return result;
    }
}
