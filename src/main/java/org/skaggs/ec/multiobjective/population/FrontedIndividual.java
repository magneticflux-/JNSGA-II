package org.skaggs.ec.multiobjective.population;

import org.skaggs.ec.population.EvaluatedIndividual;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class FrontedIndividual<E> extends EvaluatedIndividual<E> {
    private Front<E> front;

    public FrontedIndividual(EvaluatedIndividual<E> individual, Front<E> front) {
        super(individual, individual.getScores());
        this.front = front;
    }

    public Front<E> getFront() {
        return front;
    }
}
