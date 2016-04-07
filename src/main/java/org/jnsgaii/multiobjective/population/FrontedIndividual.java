package org.jnsgaii.multiobjective.population;

import org.jnsgaii.population.individual.EvaluatedIndividual;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class FrontedIndividual<E> extends EvaluatedIndividual<E> implements Comparable<FrontedIndividual<E>> {

    /**
     * The individuals that this individual dominates
     */
    final List<FrontedIndividual<E>> dominatedIndividuals;
    /**
     * The SPARSENESS of this individual's score area
     */
    double crowdingScore;
    /**
     * The number of individuals that dominate this individual
     */
    int dominationCount;

    /**
     * Used only in the fronting process
     */
    int rank;

    @SuppressWarnings("unused")
    public FrontedIndividual(EvaluatedIndividual<E> individual, List<FrontedIndividual<E>> dominatedIndividuals, double crowdingScore, int dominationCount) {
        super(individual);
        this.dominatedIndividuals = new LinkedList<>(dominatedIndividuals);
        this.crowdingScore = crowdingScore;
        this.dominationCount = dominationCount;
    }

    @SuppressWarnings("unused")
    private FrontedIndividual() {
        super();
        dominatedIndividuals = new ArrayList<>();
    }

    protected FrontedIndividual(EvaluatedIndividual<E> individual) {
        super(individual);
        rank = -1; // Unset rank
        dominationCount = 0;
        dominatedIndividuals = new LinkedList<>();
        crowdingScore = 0;
    }

    @Override
    public int compareTo(FrontedIndividual<E> o) {
        //Negative if THIS object is LESS THAN o
        if (this.rank != o.rank) {
            return Integer.compare(this.rank, o.rank); // Lowest to highest
        } else if (this.crowdingScore != o.crowdingScore) {
            return -Double.compare(this.crowdingScore, o.crowdingScore); // Highest to lowest
        } else {
            return this.toString().compareTo(o.toString());
        }
    }

    @Override
    public String toString() {
        return "{" + this.individual.toString() + " " + this.crowdingScore + "}";
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        //noinspection unchecked
        FrontedIndividual<E> that = (FrontedIndividual<E>) o;

        if (Double.compare(that.crowdingScore, crowdingScore) != 0) return false;
        if (dominationCount != that.dominationCount) return false;
        if (rank != that.rank) return false;
        if (dominatedIndividuals != null ? !dominatedIndividuals.equals(that.dominatedIndividuals) : that.dominatedIndividuals != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (dominatedIndividuals != null ? dominatedIndividuals.hashCode() : 0);
        temp = Double.doubleToLongBits(crowdingScore);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + dominationCount;
        result = 31 * result + rank;
        return result;
    }

    public double getCrowdingScore() {
        return this.crowdingScore;
    }
}
