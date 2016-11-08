package org.jnsgaii.population.individual;

import java.util.*;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class EvaluatedIndividual<E> extends Individual<E> {

    protected final List<Comparator<Double>> optimizationFunctionComparators;
    protected final double[] scores;
    protected final Map<String, Object> computationResults;

    public EvaluatedIndividual(Individual<E> individual, List<Comparator<Double>> optimizationFunctionComparators, double[] scores, Map<String, Object> computationResults) {
        super(individual);
        this.optimizationFunctionComparators = optimizationFunctionComparators;
        this.scores = scores;
        this.computationResults = computationResults;
    }

    public EvaluatedIndividual(EvaluatedIndividual<E> evaluatedIndividual) {
        super(evaluatedIndividual);
        this.optimizationFunctionComparators = evaluatedIndividual.optimizationFunctionComparators;
        this.scores = evaluatedIndividual.scores;
        this.computationResults = evaluatedIndividual.computationResults;
    }

    protected EvaluatedIndividual() {
        super();
        optimizationFunctionComparators = new ArrayList<>();
        scores = new double[0];
        computationResults = new HashMap<>();
    }

    /**
     * @param other the individual to be compared with
     * @return <code>1</code> if the provided individual is dominated by the recipient of the method call<p><code>0</code> if they are evenly matched<p><code>-1</code> if the provided individual dominates the recipient of the method call
     */
    public int dominates(EvaluatedIndividual<E> other) {
        boolean thisDominatesInAtLeastOne = false;
        boolean otherDominatesInAtLeastOne = false;

        for (int i = 0; i < optimizationFunctionComparators.size(); i++) {
            int val = optimizationFunctionComparators.get(i).compare(this.getScore(i), other.getScore(i));
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

        if (optimizationFunctionComparators != null ? !optimizationFunctionComparators.equals(that.optimizationFunctionComparators) : that.optimizationFunctionComparators != null)
            return false;
        if (!Arrays.equals(scores, that.scores)) return false;
        return computationResults != null ? computationResults.equals(that.computationResults) : that.computationResults == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (optimizationFunctionComparators != null ? optimizationFunctionComparators.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(scores);
        result = 31 * result + (computationResults != null ? computationResults.hashCode() : 0);
        return result;
    }

    public Object getComputation(String computationID) {
        return computationResults.get(computationID);
    }
}

