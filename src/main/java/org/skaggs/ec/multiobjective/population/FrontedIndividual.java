package org.skaggs.ec.multiobjective.population;

import org.skaggs.ec.population.EvaluatedIndividual;
import org.skaggs.ec.population.Individual;

import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class FrontedIndividual<E> extends EvaluatedIndividual<E> {

    private Front<E> front;
    private List<? extends Individual<E>> dominatedIndividuals;
    private double crowdingDistance;

    public FrontedIndividual(EvaluatedIndividual<E> individual, Front<E> front, List<? extends Individual<E>> dominatedIndividuals, double crowdingDistance) {
        super(individual, individual.getScores());
        this.front = front;
        this.dominatedIndividuals = dominatedIndividuals;
        this.crowdingDistance = crowdingDistance;
    }

    public Front<E> getFront() {
        return this.front;
    }

    public double getCrowdingDistance() {
        return this.crowdingDistance;
    }
}
