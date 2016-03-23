package org.skaggs.ec.operators;

import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.AspectUser;
import org.skaggs.ec.properties.HasAspectRequirements;
import org.skaggs.ec.properties.HasPropertyRequirements;
import org.skaggs.ec.properties.LateUpdatingProperties;

import java.util.function.BiFunction;

/**
 * Created by skaggsm on 2/3/16.
 */
public abstract class Speciator<E> extends AspectUser<E> implements BiFunction<Individual<E>, Individual<E>, Boolean>, HasPropertyRequirements, LateUpdatingProperties, HasAspectRequirements {
    @Override
    public Boolean apply(Individual<E> individual, Individual<E> individual2) {
        return getDistance(individual, individual2) < getMaxDistance(individual, individual2);
    }

    protected abstract double getDistance(Individual<E> individual, Individual<E> individual2);

    protected abstract double getMaxDistance(Individual<E> individual, Individual<E> individual2);
}
