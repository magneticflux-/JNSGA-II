package org.skaggs.ec.population;

import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mitchell on 11/29/2015.
 */
public class EvaluatedPopulation<E> extends Population<E> {

    /**
     * This method evaluates every individual in the population when instantiated
     *
     * @param population            the population to be evaluated
     * @param optimizationFunctions the functions to use to evaluate the population
     */
    public EvaluatedPopulation(Population<E> population, Collection<OptimizationFunction<E>> optimizationFunctions, Properties properties) {
        super();
        if (properties.getBoolean(Key.BooleanKey.BOOLEAN_THREADED))
            this.population = population.population.parallelStream().map(individual -> new EvaluatedIndividual<>(individual,
                    optimizationFunctions.parallelStream().collect(Collectors.toMap(optimizationFunction -> optimizationFunction, (OptimizationFunction<E> optimizationFunction) -> optimizationFunction.evaluate(individual.getIndividual()))))).collect(Collectors.toCollection(ArrayList::new));
        else
            this.population = population.population.stream().map(individual -> new EvaluatedIndividual<>(individual, optimizationFunctions.stream().collect(Collectors.toMap(optimizationFunction -> optimizationFunction, (OptimizationFunction<E> optimizationFunction) -> optimizationFunction.evaluate(individual.getIndividual()))))).collect(Collectors.toCollection(ArrayList::new));

        // *throws salt over shoulder*
    }

    protected EvaluatedPopulation() {
    }

    @Override
    public List<? extends EvaluatedIndividual<E>> getPopulation() {
        // This SHOULD work, since the only constructor fills the List<> with EvaluatedIndividual<>s
        //noinspection unchecked
        return Collections.unmodifiableList((List<EvaluatedIndividual<E>>) this.population);
    }
}
