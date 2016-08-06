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
import java.util.Map;
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

        for (int computationIndex = 0; computationIndex < computations.size(); computationIndex++) {
            stopWatch.start();

            Computation<E, ?> computation = computations.get(computationIndex);

            Map<Integer, Integer> toBeComputedIndexes = new HashMap<>(); // Population index -> result index
            List<Individual<E>> individualsToBeComputed = new ArrayList<>();
            for (int resultIndex = 0, populationIndex = 0; populationIndex < population.size(); populationIndex++) {
                if (!computation.isDeterministic() || !(population.getPopulation().get(populationIndex) instanceof EvaluatedIndividual)) {
                    toBeComputedIndexes.put(populationIndex, resultIndex++);
                    individualsToBeComputed.add(population.getPopulation().get(populationIndex));
                }
            }

            @SuppressWarnings("unchecked")
            Object[] results = computation.compute(individualsToBeComputed, properties);

            IntStream.range(0, population.size()).parallel().forEach(populationIndex -> {
                if (computationResults[populationIndex] == null)
                    computationResults[populationIndex] = new HashMap<>();

                if (toBeComputedIndexes.containsKey(populationIndex)) { // It needed to be computed
                    computationResults[populationIndex].put(computation.getComputationID(), results[toBeComputedIndexes.get(populationIndex)]); // Look up where it was stored in the results array
                } else { // The data can be reused
                    computationResults[populationIndex].put(computation.getComputationID(), ((EvaluatedIndividual<E>) population.getPopulation().get(populationIndex)).getComputation(computation.getComputationID())); // The individual must have been EvaluatedIndividual, cast and retrieve the data
                }
            });

            stopWatch.stop();
            computationTimes[computationIndex] = stopWatch.getNanoTime();
            stopWatch.reset();
        }

        for (int i = 0; i < optimizationFunctions.size(); i++) {
            stopWatch.start();

            OptimizationFunction<E> optimizationFunction = optimizationFunctions.get(i);

            Map<Integer, Integer> toBeComputedIndexes = new HashMap<>(); // Population index -> result index
            List<Individual<E>> individualsToBeComputed = new ArrayList<>();
            for (int resultIndex = 0, populationIndex = 0; populationIndex < population.size(); populationIndex++) {
                if (!optimizationFunction.isDeterministic() || !(population.getPopulation().get(populationIndex) instanceof EvaluatedIndividual)) {
                    toBeComputedIndexes.put(populationIndex, resultIndex++);
                    individualsToBeComputed.add(population.getPopulation().get(populationIndex));
                }
            }

            double[] results = optimizationFunction.evaluate(individualsToBeComputed, computationResults, properties); // See the computation section for details
            final int finalI = i;
            IntStream.range(0, scores.length).parallel().forEach(populationIndex -> {
                        //scores[populationIndex][finalI] = results[populationIndex];
                        if (toBeComputedIndexes.containsKey(populationIndex)) {
                            scores[populationIndex][finalI] = results[toBeComputedIndexes.get(populationIndex)];
                        } else {
                            scores[populationIndex][finalI] = ((EvaluatedIndividual<E>) population.getPopulation().get(populationIndex)).getScore(finalI);
                        }
                    }
            );

            stopWatch.stop();
            optimizationFunctionTimes[i] = stopWatch.getNanoTime();
            stopWatch.reset();

        }
        List<EvaluatedIndividual<E>> newPopulation = new ArrayList<>(properties.getInt(Key.IntKey.DefaultIntKey.POPULATION_SIZE));
        for (int i = 0; i < population.size(); i++) {
            //noinspection unchecked
            newPopulation.add(new EvaluatedIndividual<>(population.getPopulation().get(i), optimizationFunctions, scores[i], computationResults[i]));
        }

        this.population = newPopulation;
    }

    private EvaluatedPopulation(List<EvaluatedIndividual<E>> individuals) {
        this();
        this.population = new ArrayList<>(individuals);
    }

    protected EvaluatedPopulation() {
        computationTimes = new long[0];
        optimizationFunctionTimes = new long[0];
    }

    public static <E> EvaluatedPopulation<E> merge(EvaluatedPopulation<E> population1, EvaluatedPopulation<E> population2) {
        ArrayList<EvaluatedIndividual<E>> individuals = new ArrayList<>(population1.size() + population2.size());
        individuals.addAll(population1.getPopulation());
        individuals.addAll(population2.getPopulation());
        return new EvaluatedPopulation<>(individuals);
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
