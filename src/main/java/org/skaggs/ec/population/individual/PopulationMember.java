package org.skaggs.ec.population.individual;

/**
 * Created by Mitchell on 1/15/2016.
 */
public abstract class PopulationMember {
    public final double mutationStrength;
    public final double mutationProbability;
    public final double crossoverStrength;
    public final double crossoverProbability;
    public final double[] extraParameters;



    @Deprecated
    public PopulationMember(double mutationStrength, double mutationProbability, double crossoverStrength, double crossoverProbability) {
        this.mutationStrength = mutationStrength;
        this.mutationProbability = mutationProbability;
        this.crossoverStrength = crossoverStrength;
        this.crossoverProbability = crossoverProbability;
        extraParameters = new double[0];
    }

    public PopulationMember(PopulationMember populationMember) {
        this.extraParameters = populationMember.extraParameters.clone();
        this.mutationStrength = populationMember.mutationStrength;
        this.mutationProbability = populationMember.mutationProbability;
        this.crossoverStrength = populationMember.crossoverStrength;
        this.crossoverProbability = populationMember.crossoverProbability;
    }
}
