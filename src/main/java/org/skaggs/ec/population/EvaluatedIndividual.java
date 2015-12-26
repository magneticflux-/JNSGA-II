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

    public Double getScore(OptimizationFunction<E> optimizationFunction) {
        return this.scores.get(optimizationFunction);
    }


    /**
     * @param o the individual to be compared with
     * @return <code>true</code> if the provided individual is dominated by the recipient of the call
     */
    public boolean dominates(EvaluatedIndividual<E> o) {
        boolean isAtLeastEqualToForAll = true;
        boolean greaterThanAtLeastOne = false;

        assert this.getScores().keySet().equals(o.getScores().keySet()); // They should NEVER be different

        loop:
        for (OptimizationFunction<E> function : this.getScores().keySet()) {
            switch (function.compare(this.getScores().get(function), o.getScores().get(function))) {
                case -1: // If 'this' has a worse score than 'o'
                    isAtLeastEqualToForAll = false;
                    break loop; // Fail-fast because 'this' can never dominate 'o'
                case 0: // If 'this' has the same score than 'o'
                    break; // test remains true, for now...
                case 1: // If 'this' has a better score than 'o'
                    greaterThanAtLeastOne = true; // 'this' has to be better than 'o' at something to dominate it
                    break;
            }
        }

        return greaterThanAtLeastOne && isAtLeastEqualToForAll;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EvaluatedIndividual && ((EvaluatedIndividual) obj).getIndividual().equals(this.getIndividual()) && ((EvaluatedIndividual) obj).getScores().equals(this.getScores());
    }
}
