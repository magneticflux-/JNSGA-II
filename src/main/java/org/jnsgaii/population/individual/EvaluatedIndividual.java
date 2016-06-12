package org.jnsgaii.population.individual;

import org.jnsgaii.functions.OptimizationFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class EvaluatedIndividual<E> extends Individual<E> {

    protected final List<OptimizationFunction<E>> optimizationFunctions;
    protected final double[] scores;

    @SuppressWarnings({"AssignmentToCollectionOrArrayFieldFromParameter", "MethodCanBeVariableArityMethod"})
    public EvaluatedIndividual(Individual<E> individual, List<OptimizationFunction<E>> optimizationFunctions, double[] scores) {
        super(individual);
        this.optimizationFunctions = optimizationFunctions;
        this.scores = scores;
    }

    public EvaluatedIndividual(EvaluatedIndividual<E> evaluatedIndividual) {
        super(evaluatedIndividual);
        optimizationFunctions = evaluatedIndividual.optimizationFunctions;
        scores = evaluatedIndividual.scores;
    }

    protected EvaluatedIndividual() {
        super();
        optimizationFunctions = new ArrayList<>();
        scores = new double[0];
    }

    /**
     * @param other the individual to be compared with
     * @return <code>1</code> if the provided individual is dominated by the recipient of the method call<p><code>0</code> if they are evenly matched<p><code>-1</code> if the provided individual dominates the recipient of the method call
     */
    public int dominates(EvaluatedIndividual<E> other) {
        boolean thisDominatesInAtLeastOne = false;
        boolean otherDominatesInAtLeastOne = false;

        for (int i = 0; i < optimizationFunctions.size(); i++) {
            int val = optimizationFunctions.get(i).compare(this.getScore(i), other.getScore(i));
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

    public double[] getScores() {
        return this.scores.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        EvaluatedIndividual<?> that = (EvaluatedIndividual<?>) o;

        if (optimizationFunctions != null ? !optimizationFunctions.equals(that.optimizationFunctions) : that.optimizationFunctions != null)
            return false;
        return Arrays.equals(scores, that.scores);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (optimizationFunctions != null ? optimizationFunctions.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(scores);
        return result;
    }
}

