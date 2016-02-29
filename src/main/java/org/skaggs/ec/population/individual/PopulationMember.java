package org.skaggs.ec.population.individual;

/**
 * Created by Mitchell on 1/15/2016.
 */
public abstract class PopulationMember {
    public final double[] aspects;
    private final double mutationStrength;
    private final double mutationProbability;
    private final double crossoverStrength;
    private final double crossoverProbability;
    @Deprecated
    public PopulationMember(double mutationStrength, double mutationProbability, double crossoverStrength, double crossoverProbability) {
        this.mutationStrength = mutationStrength;
        this.mutationProbability = mutationProbability;
        this.crossoverStrength = crossoverStrength;
        this.crossoverProbability = crossoverProbability;
        aspects = new double[]{crossoverStrength, crossoverProbability, mutationStrength, mutationProbability};
    }

    public PopulationMember(double[] aspects) {
        this.aspects = aspects.clone();
        mutationStrength = -1;
        mutationProbability = -1;
        crossoverStrength = -1;
        crossoverProbability = -1;
    }
    public PopulationMember(PopulationMember populationMember) {
        this.aspects = populationMember.aspects.clone();
        this.mutationStrength = populationMember.mutationStrength;
        this.mutationProbability = populationMember.mutationProbability;
        this.crossoverStrength = populationMember.crossoverStrength;
        this.crossoverProbability = populationMember.crossoverProbability;
    }

    public double getMutationStrength() {
        return aspects[2];
    }

    public double getMutationProbability() {
        return aspects[3];
    }

    public double getCrossoverStrength() {
        return aspects[0];
    }

    public double getCrossoverProbability() {
        return aspects[1];
    }
}
