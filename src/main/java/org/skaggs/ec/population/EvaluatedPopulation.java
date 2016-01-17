package org.skaggs.ec.population;

import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.population.individual.EvaluatedIndividual;
import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.util.*;
import java.util.stream.Stream;

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
    @SuppressWarnings("AssignmentToSuperclassField")
    public EvaluatedPopulation(Population<E> population, Collection<OptimizationFunction<E>> optimizationFunctions, Properties properties) {
        super();
        Stream<? extends Individual<E>> individualStream;
        if (properties.getBoolean(Key.BooleanKey.THREADED)) {
            individualStream = population.population.parallelStream();
        } else {
            individualStream = population.population.stream();
        }

        this.population = new ArrayList<EvaluatedIndividual<E>>(properties.getInt(Key.IntKey.POPULATION_SIZE));

        individualStream.forEach(individual -> {
            HashMap<OptimizationFunction<E>, Double> scores = new HashMap<>(optimizationFunctions.size());
            for (OptimizationFunction<E> optimizationFunction : optimizationFunctions) {
                scores.put(optimizationFunction, optimizationFunction.evaluate(individual.getIndividual(), properties));
            }
            EvaluatedIndividual<E> evaluatedIndividual = new EvaluatedIndividual<>(individual, scores);
            //noinspection SynchronizeOnNonFinalField
            synchronized (EvaluatedPopulation.this.population) {
                //noinspection unchecked
                ((Collection<EvaluatedIndividual<E>>) EvaluatedPopulation.this.population).add(evaluatedIndividual);
            }
        });

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
