package org.skaggs.ec.multiobjective.population;

import org.skaggs.ec.population.EvaluatedPopulation;

import java.util.Collections;
import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class FrontedPopulation<E> extends EvaluatedPopulation<E> {

    protected List<Front<E>> fronts;

    public FrontedPopulation(EvaluatedPopulation<E> population) {
        super();
        this.population = null; // TODO make code to transform an EvaluatedPopulation<E> into a FrontedPopulation<E>
        this.fronts = null;
    }

    @Override
    public List<? extends FrontedIndividual<E>> getPopulation() {
        // This SHOULD work, since the only constructor fills the List<> with FrontedIndividual<>s
        //noinspection unchecked
        return Collections.unmodifiableList((List<FrontedIndividual<E>>) this.population);
    }

    public FrontedPopulation<E> truncate(int limit) {
        return null;
    }
}
