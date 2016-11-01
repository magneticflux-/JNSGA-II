package org.jnsgaii.population.individual;

import java.util.Arrays;

/**
 * Created by Mitchell on 1/15/2016.
 */
public abstract class PopulationMember {
    public final double[] aspects; // Strength1, Probability1, Strength2, Probability2...
    public final long id;

    public PopulationMember(double[] aspects, long id) {
        this.aspects = aspects.clone();
        this.id = id;
    }

    public PopulationMember(PopulationMember populationMember) {
        this.aspects = populationMember.aspects.clone();
        this.id = populationMember.id;
    }

    PopulationMember() {
        aspects = new double[0];
        id = -1;
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

        if (id != that.id) return false;
        return Arrays.equals(aspects, that.aspects);

    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(aspects);
        result = 31 * result + (int) (id ^ (id >>> 32));
        return result;
    }
}
