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

    /**
     * @param other the individual to be compared with
     * @return <code>1</code> if the provided individual is dominated by the recipient of the method call<p><code>0</code> if they are evenly matched<p><code>-1</code> if the provided individual dominates the recipient of the method call
     */
    public int dominates(EvaluatedIndividual<E> other) {
        boolean thisDominatesInAtLeastOne = false;
        boolean otherDominatesInAtLeastOne = false;

        for (int i = 0; i < optimizationFunctions.length; i++) {
            int val = optimizationFunctions[i].compare(this.getScore(i), other.getScore(i));
            if (val < 0) {
                otherDominatesInAtLeastOne = true;
            } else if (val > 0) {
                thisDominatesInAtLeastOne = true;
            }
        }

        if (thisDominatesInAtLeastOne && !otherDominatesInAtLeastOne)
            return 1;
        else if (otherDominatesInAtLeastOne && !thisDominatesInAtLeastOne)
            return -1;
        else
            return 0;
    }

    public double getScore(int i) {
        return this.scores[i];
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(optimizationFunctions);
        result = 31 * result + Arrays.hashCode(scores);
        return result;
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
}
