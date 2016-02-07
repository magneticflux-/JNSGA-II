package org.skaggs.ec.operators;

import org.skaggs.ec.population.individual.EvaluatedIndividual;
import org.skaggs.ec.properties.HasPropertyRequirements;

import java.util.List;
import java.util.function.Function;

/**
 * Created by skaggsm on 2/3/16.
 */
public abstract class Selector<E> implements Function<List<EvaluatedIndividual<E>>, EvaluatedIndividual<E>>, HasPropertyRequirements {
    @Override
    public EvaluatedIndividual<E> apply(List<EvaluatedIndividual<E>> individuals) {
        return choose(individuals);
    }

    protected abstract EvaluatedIndividual<E> choose(List<EvaluatedIndividual<E>> individuals);
}
