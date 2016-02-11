package org.skaggs.ec.operators;

import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.properties.Key;

import java.util.List;

/**
 * Created by Mitchell on 2/10/2016.
 */
public class StochasticAcceptanceRouletteWheelLinearSelection<E> extends Selector<E> {
    @Override
    protected FrontedIndividual<E> choose(List<FrontedIndividual<E>> frontedIndividuals) {
        return null;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[0];
    }
}
