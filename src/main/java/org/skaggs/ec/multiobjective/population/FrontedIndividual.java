package org.skaggs.ec.multiobjective.population;

import org.skaggs.ec.population.EvaluatedIndividual;
import org.skaggs.ec.population.Individual;

import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class FrontedIndividual<E> extends EvaluatedIndividual<E> implements Comparable<FrontedIndividual<E>> {

    Front<E> front;
    /**
     * The individuals that this individual dominates
     */
    List<FrontedIndividual<E>> dominatedIndividuals;
    /**
     * The SPARSENESS of this individual's score area
     */
    double crowdingScore;
    /**
     * The number of individuals that dominate this individual
     */
    int dominationCount;
    int rank;

    public FrontedIndividual(EvaluatedIndividual<E> individual, Front<E> front, List<FrontedIndividual<E>> dominatedIndividuals, double crowdingScore, int dominationCount) {
        super(individual, individual.getScores());
        this.front = front;
        this.dominatedIndividuals = dominatedIndividuals;
        this.crowdingScore = crowdingScore;
        this.dominationCount = dominationCount;
    }

    FrontedIndividual(EvaluatedIndividual<E> individual) {
        super(individual, individual.getScores());
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
        return this.individual.toString() + " " + this.crowdingScore;
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public boolean oldEquals(Object obj) {
        try {
            return (obj instanceof FrontedIndividual)
                    && ((Individual) obj).getIndividual().equals(this.getIndividual())
                    && ((EvaluatedIndividual) obj).getScores().equals(this.getScores())
                    && ((FrontedIndividual) obj).getFront().equals(this.getFront())
                    && (((FrontedIndividual) obj).getCrowdingScore() == this.getCrowdingScore())
                    && ((FrontedIndividual) obj).dominatedIndividuals.equals(this.dominatedIndividuals)
                    && (((FrontedIndividual) obj).dominationCount == this.dominationCount)
                    && (((FrontedIndividual) obj).rank == this.rank);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + (front != null ? front.hashCode() : 0);
        result = 31 * result + (dominatedIndividuals != null ? dominatedIndividuals.hashCode() : 0);
        temp = Double.doubleToLongBits(crowdingScore);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + dominationCount;
        result = 31 * result + rank;
        return result;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        FrontedIndividual that = (FrontedIndividual) o;

        if (Double.compare(that.crowdingScore, crowdingScore) != 0) return false;
        if (dominationCount != that.dominationCount) return false;
        if (rank != that.rank) return false;
        if (dominatedIndividuals != null ? !dominatedIndividuals.equals(that.dominatedIndividuals) : that.dominatedIndividuals != null)
            return false;
        if (front != null ? !front.equals(that.front) : that.front != null) return false;

        return true;
    }

    public Front<E> getFront() {
        return this.front;
    }

    public double getCrowdingScore() {
        return this.crowdingScore;
    }
}
