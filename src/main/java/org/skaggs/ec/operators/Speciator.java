package org.skaggs.ec.operators;

import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.HasPropertyRequirements;

import java.util.function.BiFunction;

/**
 * Created by skaggsm on 2/3/16.
 */
public abstract class Speciator<E> implements BiFunction<Individual<E>, Individual<E>, Double>, HasPropertyRequirements {
    @Override
    public Double apply(Individual<E> individual, Individual<E> individual2) {
        return getDistance(individual, individual2);
    }

    protected abstract double getDistance(Individual<E> individual, Individual<E> individual2);

    protected abstract double getMaxDistance();
}
