package org.skaggs.ec.population;

import org.skaggs.ec.OptimizationFunction;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class EvaluatedIndividual<E> extends Individual<E> {

    protected final Map<? extends OptimizationFunction<E>, Double> scores;

    public EvaluatedIndividual(Individual<E> individual, Map<? extends OptimizationFunction<E>, Double> scores) {
        super(individual.getIndividual());
        this.scores = Collections.unmodifiableMap(scores);
    }

    public Map<? extends OptimizationFunction<E>, Double> getScores() {
        return this.scores;
    }
}
