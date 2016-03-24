package org.jnsgaii.population;

import org.jnsgaii.OptimizationFunction;
import org.jnsgaii.population.individual.EvaluatedIndividual;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

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
    public EvaluatedPopulation(Population<E> population, OptimizationFunction<E>[] optimizationFunctions, Properties properties) {
        super();

        double[][] scores = new double[population.size()][optimizationFunctions.length];

        for (int i = 0; i < optimizationFunctions.length; i++) {
            @SuppressWarnings("unchecked")
            double[] functionScores = optimizationFunctions[i].evaluate((List<Individual<E>>) population.getPopulation(), properties);
            final int finalI = i; // It _is_ effectively final! Ugh.
            IntStream.range(0, scores.length).parallel().forEach(
                    value -> scores[value][finalI] = functionScores[value]
            );
        }
        List<EvaluatedIndividual<E>> newPopulation = new ArrayList<>(properties.getInt(Key.IntKey.DefaultIntKey.POPULATION_SIZE));
        for (int i = 0; i < population.size(); i++) {
            newPopulation.add(new EvaluatedIndividual<>(population.getPopulation().get(i), optimizationFunctions, scores[i]));
        }

        this.population = newPopulation;
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
