package org.jnsgaii.population.individual;

import java.util.Arrays;

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

    PopulationMember() {
        aspects = new double[0];
    }

    @Deprecated
    public double getMutationStrength() {
        return aspects[2];
    }

    @Deprecated
    public double getMutationProbability() {
        return aspects[3];
    }

    @Deprecated
    public double getCrossoverStrength() {
        return aspects[0];
    }

    @Deprecated
    public double getCrossoverProbability() {
        return aspects[1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PopulationMember that = (PopulationMember) o;

        return Arrays.equals(aspects, that.aspects);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(aspects);
    }
}
