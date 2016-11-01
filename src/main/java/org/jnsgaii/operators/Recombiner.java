package org.jnsgaii.operators;

import org.jnsgaii.properties.AspectUser;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.LateUpdatingProperties;
import org.jnsgaii.util.Range;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by skaggsm on 1/22/16.
 */
public abstract class Recombiner<E> extends AspectUser implements HasPropertyRequirements, LateUpdatingProperties {

    @Override
    public int requestAspectLocation(int startIndex) {
        super.requestAspectLocation(startIndex);
        return 2;
    }

    @Override
    public String[] getAspectDescriptions() {
        return new String[]{"Crossover Strength", "Crossover Probability"};
    }

    public double[] apply(double[] tAspects, double[] uAspects) {
        Random r = ThreadLocalRandom.current();

        double[] result = new double[tAspects.length];
        Arrays.setAll(result, value -> (tAspects[value] + uAspects[value]) / 2d);

        if (r.nextDouble() < result[startIndex + 1])
            return result;
        else return tAspects.clone();
    }

    public E apply(E t, E u, double[] aspects) {
        Random r = ThreadLocalRandom.current();

        boolean doCrossover = r.nextDouble() < aspects[startIndex + 1];

        E individual = doCrossover ? crossover(t, u, aspects[startIndex], aspects[startIndex + 1]) : t;

        return individual;
    }

    @Override
    public void modifyAspects(double[] aspects, Random r) {
        if (r.nextDouble() < aspectModificationArray[startIndex * 2 + 1])
            aspects[startIndex] = Mutator.mutate(aspects[startIndex], r, aspectModificationArray[startIndex * 2]);
        aspects[startIndex] = Range.clip(0, aspects[0], Double.POSITIVE_INFINITY);

        if (r.nextDouble() < aspectModificationArray[startIndex * 2 + 3])
            aspects[startIndex + 1] = Mutator.mutate(aspects[startIndex + 1], r, aspectModificationArray[startIndex * 2 + 2]);
        aspects[startIndex + 1] = Range.clip(0, aspects[startIndex + 1], 1);
    }

    protected abstract E crossover(E parent1, E parent2, double crossoverStrength, double crossoverProbability);

    @Override
    public Key[] requestProperties() {
        return new Key[]{
        };
    }
}
