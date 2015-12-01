package org.skaggs.ec.multiobjective.population;

import org.skaggs.ec.population.EvaluatedIndividual;
import org.skaggs.ec.population.EvaluatedPopulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Mitchell on 11/28/2015.
 */
public class FrontedPopulation<E> extends EvaluatedPopulation<E> {

    protected List<Front<E>> fronts;

    public FrontedPopulation(EvaluatedPopulation<E> population) { // TODO make code to transform an EvaluatedPopulation<E> into a FrontedPopulation<E>
        super();

        for (EvaluatedIndividual<E> evaluatedIndividual : population.getPopulation()) {
            FrontedIndividual<E> newIndividual = new FrontedIndividual<>(evaluatedIndividual);
            newIndividual.dominationCount = 0;
            newIndividual.dominatedIndividuals = new ArrayList<>();
            for (EvaluatedIndividual<E> other : population.getPopulation()) {
            }
        }

        this.population = null;
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
