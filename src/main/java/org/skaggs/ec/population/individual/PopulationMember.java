package org.skaggs.ec.population.individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mitchell on 1/15/2016.
 */
public abstract class PopulationMember {
    public final double mutationStrength;
    public final double mutationProbability;
    public final double crossoverStrength;
    public final double crossoverProbability;
    public final List<Double> parameters;

    public PopulationMember(ArrayList<Double> parameters) {
        this.parameters = Collections.unmodifiableList(new ArrayList<>(parameters));
        mutationStrength = 0;
        mutationProbability = 0;
        crossoverStrength = 0;
        crossoverProbability = 0;
    }

    @Deprecated
    public PopulationMember(double mutationStrength, double mutationProbability, double crossoverStrength, double crossoverProbability) {
        this.mutationStrength = mutationStrength;
        this.mutationProbability = mutationProbability;
        this.crossoverStrength = crossoverStrength;
        this.crossoverProbability = crossoverProbability;
        parameters = Collections.unmodifiableList(new ArrayList<>());
    }

    public PopulationMember(PopulationMember populationMember) {
        this.parameters = populationMember.parameters;
        this.mutationStrength = populationMember.mutationStrength;
        this.mutationProbability = populationMember.mutationProbability;
        this.crossoverStrength = populationMember.crossoverStrength;
        this.crossoverProbability = populationMember.crossoverProbability;
    }
}
