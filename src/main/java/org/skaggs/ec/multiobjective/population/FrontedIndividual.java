package org.skaggs.ec.multiobjective.population;

import org.skaggs.ec.population.EvaluatedIndividual;
import org.skaggs.ec.population.Individual;

import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class FrontedIndividual<E> extends EvaluatedIndividual<E> implements Comparable<FrontedIndividual<E>> {

    Front<E> front;
    List<? extends Individual<E>> dominatedIndividuals;
    double crowdingDistance;
    int dominationCount;
    int rank;

    public FrontedIndividual(EvaluatedIndividual<E> individual, Front<E> front, List<? extends Individual<E>> dominatedIndividuals, double crowdingDistance, int dominationCount) {
        super(individual, individual.getScores());
        this.front = front;
        this.dominatedIndividuals = dominatedIndividuals;
        this.crowdingDistance = crowdingDistance;
    }

    FrontedIndividual(EvaluatedIndividual<E> individual) {
        super(individual, individual.getScores());
    }


    public Front<E> getFront() {
        return this.front;
    }

    public double getCrowdingDistance() {
        return this.crowdingDistance;
    }

    @Override
    public int compareTo(FrontedIndividual<E> o) {
        //Negative if THIS object is LESS THAN o
        if (this.rank != o.rank) {
            return Integer.compare(this.rank, o.rank);
        } else {
            return Double.compare(this.crowdingDistance, o.crowdingDistance);
        }
    }
}
