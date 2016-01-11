package org.skaggs.ec.population;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class Individual<E> {

    protected final E individual;
    protected final double mutationProbability;
    protected final double crossoverProbability;

    @Deprecated
    public Individual(E individual) {
        this.individual = individual;
        mutationProbability = 0;
        crossoverProbability = 0;
    }

    public Individual(E individual, double mutationProbability, double crossoverProbability) {
        this.individual = individual;
        this.mutationProbability = mutationProbability;
        this.crossoverProbability = crossoverProbability;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    @Deprecated
    public boolean oldEquals(Object obj) {
        return obj instanceof Individual &&
                ((Individual) obj).getIndividual().equals(this.getIndividual()) &&
                ((Individual) obj).mutationProbability == this.mutationProbability &&
                ((Individual) obj).crossoverProbability == this.crossoverProbability;
    }

    public E getIndividual() {
        return this.individual;
    }

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
