package org.skaggs.ec.population.individual;

import org.skaggs.ec.OptimizationFunction;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class EvaluatedIndividual<E> extends Individual<E> {

    protected final Map<? extends OptimizationFunction<E>, Double> scores;

    public EvaluatedIndividual(Individual<E> individual, Map<? extends OptimizationFunction<E>, Double> scores) {
        super(individual);
        this.scores = Collections.unmodifiableMap(scores);
    }

    public EvaluatedIndividual(EvaluatedIndividual<E> evaluatedIndividual) {
        super(evaluatedIndividual);
        this.scores = evaluatedIndividual.scores;
    }

    public Double getScore(OptimizationFunction<E> optimizationFunction) {
        return this.scores.get(optimizationFunction);
    }

    /**
     * @param other the individual to be compared with
     * @return <code>1</code> if the provided individual is dominated by the recipient of the method call<p><code>0</code> if they are evenly matched<p><code>-1</code> if the provided individual dominates the recipient of the method call
     */
    public int dominates(EvaluatedIndividual<E> other) {
        //boolean isAtLeastEqualToForAll = true;
        //boolean greaterThanAtLeastOne = false;
        boolean thisDominatesInAtLeastOne = false;
        boolean otherDominatesInAtLeastOne = false;

        //assert this.getScores().keySet().equals(o.getScores().keySet()); // They should NEVER be different

        //loop:
        for (Map.Entry<? extends OptimizationFunction<E>, Double> entry : this.scores.entrySet()) {
            OptimizationFunction<E> function = entry.getKey();
            switch (function.compare(this.getScores().get(function), other.getScores().get(function))) {
                case -1: // If 'this' has a worse score than 'o'
                    //isAtLeastEqualToForAll = false;
                    otherDominatesInAtLeastOne = true;
                    //break loop; // Fail-fast because 'this' can never dominate 'o'
                    break;
                //case 0: // If 'this' has the same score than 'o'
                //    break; // test remains true, for now...
                case 1: // If 'this' has a better score than 'o'
                    //greaterThanAtLeastOne = true; // 'this' has to be better than 'o' at something to dominate it
                    thisDominatesInAtLeastOne = true;
                    break;
                default:
                    break;
            }
        }

        if (thisDominatesInAtLeastOne && !otherDominatesInAtLeastOne)
            return 1;
        else if (otherDominatesInAtLeastOne && !thisDominatesInAtLeastOne)
            return -1;
        else
            return 0;
    }

    public Map<? extends OptimizationFunction<E>, Double> getScores() {
        return this.scores;
    }

    //Old method just in case
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public boolean oldEquals(Object obj) {
        return obj instanceof EvaluatedIndividual && ((Individual) obj).getIndividual().equals(this.getIndividual()) && ((EvaluatedIndividual) obj).getScores().equals(this.getScores());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + scores.hashCode();
        return result;
    }

    @SuppressWarnings("RedundantIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        EvaluatedIndividual that = (EvaluatedIndividual) o;

        if (!scores.equals(that.scores)) return false;

        return true;
    }
}
