package org.jnsgaii.operators;

import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.AspectUser;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.LateUpdatingProperties;
import org.jnsgaii.util.Range;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

/**
 * Created by skaggsm on 1/22/16.
 */
public abstract class Mutator<E> extends AspectUser<E> implements Function<Individual<E>, Individual<E>>, HasPropertyRequirements, LateUpdatingProperties {

    public static double mutate(double d, Random r, double range) {
        return (d + (r.nextDouble() * 2 * range)) - range;
    }

    @Override
    public Individual<E> apply(Individual<E> e) {
        Random r = ThreadLocalRandom.current();

        double[] aspects = e.aspects.clone();

        E individual = e.getIndividual();

        if (r.nextDouble() < aspects[startIndex + 1])
            individual = mutate(e.getIndividual(), aspects[startIndex], aspects[startIndex + 1]);


        return new Individual<>(individual, aspects);
    }

    @Override
    public void modifyAspects(Individual<E> individual, Random r) {
        double[] aspects = individual.aspects;

        if (r.nextDouble() < aspectModificationArray[startIndex * 2 + 1])
            aspects[startIndex] = Mutator.mutate(aspects[startIndex], r, aspectModificationArray[startIndex * 2]);
        if (r.nextDouble() < aspectModificationArray[startIndex * 2 + 3])
            aspects[startIndex + 1] = Mutator.mutate(aspects[startIndex + 1], r, aspectModificationArray[startIndex * 2 + 2]);

        aspects[startIndex] = Range.clip(0, aspects[startIndex], Double.POSITIVE_INFINITY);
        aspects[startIndex + 1] = Range.clip(0, aspects[startIndex + 1], 1);
    }

    @Override
    public String[] getAspectDescriptions() {
        return new String[]{"Mutation Strength", "Mutation Probability"};
    }

    @Override
    public int requestAspectLocation(int startIndex) {
        super.requestAspectLocation(startIndex);
        return 2;
    }

    protected abstract E mutate(E object, double mutationStrength, double mutationProbability);
}
