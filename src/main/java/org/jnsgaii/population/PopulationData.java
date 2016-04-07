package org.jnsgaii.population;

import org.jnsgaii.multiobjective.population.FrontedPopulation;

/**
 * Created by Mitchell on 11/25/2015.
 * <p>
 * TODO Add stats about the frontedPopulation. Mean, min, max, standard deviation
 */
public class PopulationData<E> {
    private final FrontedPopulation<E> frontedPopulation;
    private final FrontedPopulation<E> truncatedPopulation;
    private final long elapsedTime;
    private final long previousObservationTime;
    private final int currentGeneration;

    @SuppressWarnings("unused")
    private PopulationData() {
        this(null, null, -1, -1, -1);
    }

    public PopulationData(FrontedPopulation<E> frontedPopulation, FrontedPopulation<E> truncatedPopulation, long elapsedTime, long previousObservationTime, int currentGeneration) {
        this.frontedPopulation = frontedPopulation;
        this.truncatedPopulation = truncatedPopulation;
        this.elapsedTime = elapsedTime;
        this.currentGeneration = currentGeneration;
        this.previousObservationTime = previousObservationTime;
    }

    public FrontedPopulation<E> getFrontedPopulation() {
        return this.frontedPopulation;
    }

    public FrontedPopulation<E> getTruncatedPopulation() {
        return this.truncatedPopulation;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public int getCurrentGeneration() {
        return currentGeneration;
    }

    public long getPreviousObservationTime() {
        return previousObservationTime;
    }
}
