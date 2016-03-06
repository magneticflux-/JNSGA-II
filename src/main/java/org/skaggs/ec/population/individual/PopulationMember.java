package org.skaggs.ec.population.individual;

/**
 * Created by Mitchell on 1/15/2016.
 */
public abstract class PopulationMember {
    public final double[] aspects; // Strength1, Probability1, Strength2, Probability2...

    public PopulationMember(double[] aspects) {
        this.aspects = aspects.clone();
    }

    public PopulationMember(PopulationMember populationMember) {
        this.aspects = populationMember.aspects.clone();
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
