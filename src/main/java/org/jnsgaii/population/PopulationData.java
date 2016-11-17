package org.jnsgaii.population;

import org.jnsgaii.multiobjective.population.FrontedPopulation;

import java.util.Arrays;

/**
 * Created by Mitchell Skaggs on 11/25/2015.
 * <p>
 * TODO Add stats about the frontedPopulation. Mean, min, max, standard deviation
 */
public class PopulationData<E> {
    private final FrontedPopulation<E> frontedPopulation;
    private final FrontedPopulation<E> truncatedPopulation;
    private final long previousObservationTime;
    private final long operatorApplyingTime;
    private final long mergingTime;
    private final long[] computationTimes;
    private final long[] optimizationFunctionTimes;
    private final long frontingTime;
    private final long truncationTime;
    private final int currentGeneration;

    @SuppressWarnings("unused")
    private PopulationData() {
        this(null, null, -1, -1, null, null, -1, -1, -1, -1);
    }

    public PopulationData(FrontedPopulation<E> frontedPopulation, FrontedPopulation<E> truncatedPopulation, long operatorApplyingTime, long mergingTime, long[] computationTimes, long[] optimizationFunctionTimes, long frontingTime, long truncationTime, long previousObservationTime, int currentGeneration) {
        this.frontedPopulation = frontedPopulation;
        this.truncatedPopulation = truncatedPopulation;
        this.operatorApplyingTime = operatorApplyingTime;
        this.mergingTime = mergingTime;
        this.computationTimes = computationTimes;
        this.optimizationFunctionTimes = optimizationFunctionTimes;
        this.frontingTime = frontingTime;
        this.truncationTime = truncationTime;
        this.previousObservationTime = previousObservationTime;
        this.currentGeneration = currentGeneration;
    }

    public FrontedPopulation<E> getFrontedPopulation() {
        return this.frontedPopulation;
    }

    public FrontedPopulation<E> getTruncatedPopulation() {
        return this.truncatedPopulation;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public long getPreviousObservationTime() {
        return previousObservationTime;
    }

    public long getOperatorApplyingTime() {
        return operatorApplyingTime;
    }

    public long getMergingTime() {
        return mergingTime;
    }

    public long[] getComputationTimes() {
        return computationTimes;
    }

    public long[] getOptimizationFunctionTimes() {
        return optimizationFunctionTimes;
    }

    public long getFrontingTime() {
        return frontingTime;
    }

    public long getTruncationTime() {
        return truncationTime;
    }

    public long getTotalTime() {
        return operatorApplyingTime + mergingTime + Arrays.stream(computationTimes).sum() + Arrays.stream(optimizationFunctionTimes).sum() + frontingTime + truncationTime;
    }
}
