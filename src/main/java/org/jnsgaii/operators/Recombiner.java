package org.jnsgaii.operators;

import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.AspectUser;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.LateUpdatingProperties;
import org.jnsgaii.util.Range;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;


/**
 * Created by skaggsm on 1/22/16.
 */
public abstract class Recombiner<E> extends AspectUser<E> implements BinaryOperator<Individual<E>>, HasPropertyRequirements, LateUpdatingProperties {

    @Override
    public int requestAspectLocation(int startIndex) {
        super.requestAspectLocation(startIndex);
        return 2;
    }

    @Override
    public String[] getAspectDescriptions() {
        return new String[]{"Crossover Strength", "Crossover Probability"};
    }

    @Override
    public Individual<E> apply(Individual<E> t, Individual<E> u) {
        Random r = ThreadLocalRandom.current();

        double[] newAspects = t.aspects.clone();

        boolean doCrossover = r.nextDouble() < newAspects[startIndex + 1];

        E individual = doCrossover ? crossover(t.getIndividual(), u.getIndividual(), newAspects[startIndex], newAspects[startIndex + 1]) : t.getIndividual();

        return new Individual<>(individual, newAspects);
    }

    @Override
    public void modifyAspects(Individual<E> individual, Random r) {
        double[] aspects = individual.aspects;

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
