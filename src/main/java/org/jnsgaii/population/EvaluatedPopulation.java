package org.jnsgaii.population;

import org.apache.commons.lang3.time.StopWatch;
import org.jnsgaii.computations.Computation;
import org.jnsgaii.functions.OptimizationFunction;
import org.jnsgaii.population.individual.EvaluatedIndividual;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Mitchell on 11/29/2015.
 */
public class EvaluatedPopulation<E> extends Population<E> {

    private final long[] computationTimes, optimizationFunctionTimes;

    /**
     * This method evaluates every individual in the population when instantiated
     *
     * @param population            the population to be evaluated
     * @param optimizationFunctions the functions to use to evaluate the population
     */
    @SuppressWarnings("AssignmentToSuperclassField")
    public EvaluatedPopulation(Population<E> population, List<OptimizationFunction<E>> optimizationFunctions, List<Computation<E, ?>> computations, Properties properties) {
        super();

        StopWatch stopWatch = new StopWatch();
        computationTimes = new long[computations.size()];
        optimizationFunctionTimes = new long[optimizationFunctions.size()];


        double[][] scores = new double[population.size()][optimizationFunctions.size()];
        @SuppressWarnings("unchecked")
        HashMap<String, Object>[] computationResults = new HashMap[population.size()];

        for (int i = 0; i < computations.size(); i++) {
            stopWatch.start();

            Computation<E, ?> computation = computations.get(i);
            @SuppressWarnings("unchecked")
            Object[] results = computation.compute((List<Individual<E>>) population.getPopulation(), properties);

            IntStream.range(0, results.length).parallel().forEach(value -> {
                if (computationResults[value] == null)
                    computationResults[value] = new HashMap<>();
                computationResults[value].put(computation.getComputationID(), results[value]);
            });

            stopWatch.stop();
            computationTimes[i] = stopWatch.getNanoTime();
            stopWatch.reset();
        }

        for (int i = 0; i < optimizationFunctions.size(); i++) {
            stopWatch.start();

            @SuppressWarnings("unchecked")
            double[] functionScores = optimizationFunctions.get(i).evaluate((List<Individual<E>>) population.getPopulation(), computationResults, properties);
            final int finalI = i;
            IntStream.range(0, scores.length).parallel().forEach(
                    value -> scores[value][finalI] = functionScores[value]
            );

            stopWatch.stop();
            optimizationFunctionTimes[i] = stopWatch.getNanoTime();
            stopWatch.reset();

        }
        List<EvaluatedIndividual<E>> newPopulation = new ArrayList<>(properties.getInt(Key.IntKey.DefaultIntKey.POPULATION_SIZE));
        for (int i = 0; i < population.size(); i++) {
            newPopulation.add(new EvaluatedIndividual<>(population.getPopulation().get(i), optimizationFunctions, scores[i]));
        }

        this.population = newPopulation;
    }

    protected EvaluatedPopulation() {
        computationTimes = new long[0];
        optimizationFunctionTimes = new long[0];
    }

    @Override
    public List<? extends EvaluatedIndividual<E>> getPopulation() {
        // This SHOULD work, since the only constructor fills the List<> with EvaluatedIndividual<>s
        //noinspection unchecked
        return (List<EvaluatedIndividual<E>>) this.population;
    }

    public long[] getComputationTimes() {
        return computationTimes;
    }

    public long[] getOptimizationFunctionTimes() {
        return optimizationFunctionTimes;
    }
}
