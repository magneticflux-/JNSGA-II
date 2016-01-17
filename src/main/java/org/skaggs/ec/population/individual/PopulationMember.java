package org.skaggs.ec.population.individual;

/**
 * Created by Mitchell on 1/15/2016.
 */
public abstract class PopulationMember {
    public final double mutationProbability;
    public final double crossoverProbability;
    public final double mutationStrength;
    public final double crossoverStrength;

    public PopulationMember(double mutationStrength, double mutationProbability, double crossoverProbability, double crossoverStrength) {
        this.mutationStrength = mutationStrength;
        this.mutationProbability = mutationProbability;
        this.crossoverProbability = crossoverProbability;
        this.crossoverStrength = crossoverStrength;
    }

    public PopulationMember(PopulationMember populationMember) {
        this.mutationStrength = populationMember.mutationStrength;
        this.mutationProbability = populationMember.mutationProbability;
        this.crossoverProbability = populationMember.crossoverProbability;
        this.crossoverStrength = populationMember.crossoverStrength;
    }
}
