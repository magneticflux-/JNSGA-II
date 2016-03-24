package org.jnsgaii.operators;

import org.jnsgaii.multiobjective.population.FrontedIndividual;
import org.jnsgaii.properties.AspectUser;
import org.jnsgaii.properties.HasAspectRequirements;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.LateUpdatingProperties;

import java.util.List;
import java.util.function.Function;

/**
 * Created by skaggsm on 2/3/16.
 */
public abstract class Selector<E> extends AspectUser<E> implements Function<List<FrontedIndividual<E>>, FrontedIndividual<E>>, HasPropertyRequirements, LateUpdatingProperties, HasAspectRequirements {
    @Override
    public FrontedIndividual<E> apply(List<FrontedIndividual<E>> individuals) {
        return choose(individuals);
    }

    protected abstract FrontedIndividual<E> choose(List<FrontedIndividual<E>> individuals);
}
