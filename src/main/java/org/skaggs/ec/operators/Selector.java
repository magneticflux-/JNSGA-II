package org.skaggs.ec.operators;

import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.properties.HasAspectRequirements;
import org.skaggs.ec.properties.HasPropertyRequirements;
import org.skaggs.ec.properties.LateUpdatingProperties;

import java.util.List;
import java.util.function.Function;

/**
 * Created by skaggsm on 2/3/16.
 */
public abstract class Selector<E> implements Function<List<FrontedIndividual<E>>, FrontedIndividual<E>>, HasPropertyRequirements, LateUpdatingProperties, HasAspectRequirements {
    @Override
    public FrontedIndividual<E> apply(List<FrontedIndividual<E>> individuals) {
        return choose(individuals);
    }

    protected abstract FrontedIndividual<E> choose(List<FrontedIndividual<E>> individuals);
}
