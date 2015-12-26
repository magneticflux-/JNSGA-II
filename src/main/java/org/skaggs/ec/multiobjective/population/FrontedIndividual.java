package org.skaggs.ec.multiobjective.population;

import org.skaggs.ec.population.EvaluatedIndividual;

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
    public String toString() {
        return this.individual.toString() + " " + this.crowdingScore;
    }

    public Front<E> getFront() {
        return this.front;
    }

    public double getCrowdingScore() {
        return this.crowdingScore;
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
    public boolean equals(Object obj) {
        return obj instanceof FrontedIndividual
                && ((FrontedIndividual) obj).getIndividual().equals(this.getIndividual())
                && ((FrontedIndividual) obj).getScores().equals(this.getScores())
                && ((FrontedIndividual) obj).getFront().equals(this.getFront())
                && ((FrontedIndividual) obj).getCrowdingScore() == this.getCrowdingScore()
                && ((FrontedIndividual) obj).dominatedIndividuals.equals(this.dominatedIndividuals)
                && ((FrontedIndividual) obj).dominationCount == this.dominationCount
                && ((FrontedIndividual) obj).rank == this.rank;
    }
}
