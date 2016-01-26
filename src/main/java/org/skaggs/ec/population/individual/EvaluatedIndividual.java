package org.skaggs.ec.population.individual;

import org.skaggs.ec.OptimizationFunction;

import java.util.Arrays;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class EvaluatedIndividual<E> extends Individual<E> {

    protected final OptimizationFunction<E>[] optimizationFunctions;
    protected final double[] scores;

    @SuppressWarnings({"unchecked", "AssignmentToCollectionOrArrayFieldFromParameter"})
    public EvaluatedIndividual(Individual<E> individual, OptimizationFunction<E>[] optimizationFunctions, double[] scores) {
        super(individual);
        this.optimizationFunctions = optimizationFunctions;
        this.scores = scores;
    }

    public EvaluatedIndividual(EvaluatedIndividual<E> evaluatedIndividual) {
        super(evaluatedIndividual);
        optimizationFunctions = evaluatedIndividual.optimizationFunctions;
        scores = evaluatedIndividual.scores;
    }

    public double getScore(int i) {
        return this.scores[i];
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
        for (int i = 0; i < optimizationFunctions.length; i++) {
            switch (optimizationFunctions[i].compare(this.getScore(i), other.getScore(i))) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        EvaluatedIndividual<?> that = (EvaluatedIndividual<?>) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(optimizationFunctions, that.optimizationFunctions)) return false;
        return Arrays.equals(scores, that.scores);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(optimizationFunctions);
        result = 31 * result + Arrays.hashCode(scores);
        return result;
    }
}
