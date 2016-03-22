package org.skaggs.ec.operators;

import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.*;
import org.skaggs.ec.util.Range;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;


/**
 * Created by skaggsm on 1/22/16.
 */
public abstract class Recombiner<E> implements BiFunction<Individual<E>, Individual<E>, Individual<E>>, HasPropertyRequirements, LateUpdatingProperties, HasAspectRequirements {

    private int startIndex;
    private double[] aspectModificationArray;

    @Override
    public int requestAspectLocation(int startIndex) {
        this.startIndex = startIndex;
        return 2;
    }

    @Override
    public String[] getAspectDescriptions() {
        return new String[]{"Crossover Strength", "Crossover Probability"};
    }

    @Override
    public void updateProperties(Properties properties) {
        aspectModificationArray = (double[]) properties.getValue(Key.DoubleKey.DefaultDoubleKey.ASPECT_MODIFICATION_ARRAY);
    }

    @Override
    public Individual<E> apply(Individual<E> t, Individual<E> u) {
        Random r = ThreadLocalRandom.current();

        double[] newAspects = t.aspects.clone();

        boolean doCrossover = r.nextDouble() < newAspects[startIndex + 1];

        if (r.nextDouble() < aspectModificationArray[startIndex * 2 + 1])
            newAspects[startIndex] = Mutator.mutate(newAspects[startIndex], r, aspectModificationArray[startIndex * 2]);
        newAspects[startIndex] = Range.clip(0, newAspects[0], Double.POSITIVE_INFINITY);

        if (r.nextDouble() < aspectModificationArray[startIndex * 2 + 3])
            newAspects[startIndex + 1] = Mutator.mutate(newAspects[startIndex + 1], r, aspectModificationArray[startIndex * 2 + 2]);
        newAspects[startIndex + 1] = Range.clip(0, newAspects[startIndex + 1], 1);

        E individual = doCrossover ? crossover(t.getIndividual(), u.getIndividual(), newAspects[startIndex], newAspects[startIndex + 1]) : t.getIndividual();

        return new Individual<>(individual, newAspects);
    }

    protected abstract E crossover(E parent1, E parent2, double crossoverStrength, double crossoverProbability);

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.DoubleKey.DefaultDoubleKey.ASPECT_MODIFICATION_ARRAY
        };
    }
}
