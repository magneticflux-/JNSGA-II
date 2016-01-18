package org.skaggs.ec.population.individual;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Individual<E> extends PopulationMember {

    protected final E individual;

    @Deprecated
    public Individual(E individual) {
        this(individual, -1, -1, -1, -1);
    }

    public Individual(Individual<E> individual) {
        super(individual);
        this.individual = individual.individual;
    }

    public Individual(E individual, double mutationStrength, double mutationProbability, double crossoverStrength, double crossoverProbability) {
        super(mutationStrength, mutationProbability, crossoverProbability, crossoverStrength);
        this.individual = individual;
    }

    @SuppressWarnings("unused")
    @Deprecated
    public boolean oldEquals(Object obj) {
        return obj instanceof Individual &&
                ((Individual) obj).getIndividual().equals(this.getIndividual()) &&
                ((PopulationMember) obj).mutationProbability == this.mutationProbability &&
                ((PopulationMember) obj).crossoverProbability == this.crossoverProbability;
    }

    public E getIndividual() {
        return this.individual;
    }

    @SuppressWarnings("unused")
    @Deprecated
    public int oldHashCode() {
        return this.individual.hashCode() ^
                Double.hashCode(mutationProbability) ^
                Double.hashCode(crossoverProbability);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = individual != null ? individual.hashCode() : 0;
        temp = Double.doubleToLongBits(mutationProbability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(crossoverProbability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Individual that = (Individual) o;

        if (Double.compare(that.crossoverProbability, crossoverProbability) != 0) return false;
        if (Double.compare(that.mutationProbability, mutationProbability) != 0) return false;
        if (individual != null ? !individual.equals(that.individual) : that.individual != null)
            return false;

        return true;
    }
}
