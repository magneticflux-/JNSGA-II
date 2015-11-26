package org.skaggs.ec.population;

import org.skaggs.ec.OptimizationFunction;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class EvaluatedIndividual<E> {

    private final E individual;
    private final Map<Class<? extends OptimizationFunction<E>>, Double> scores;

    public EvaluatedIndividual(E individual, Map<Class<? extends OptimizationFunction<E>>, Double> scores) {
        this.individual = individual;
        this.scores = Collections.unmodifiableMap(scores);
    }

    public E getIndividual() {
        return individual;
    }

    public Map<Class<? extends OptimizationFunction<E>>, Double> getScores() {
        return scores;
    }
}
