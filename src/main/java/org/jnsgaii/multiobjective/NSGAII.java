package org.jnsgaii.multiobjective;

import org.apache.commons.lang3.time.StopWatch;
import org.jnsgaii.computations.Computation;
import org.jnsgaii.exceptions.NoValueSetException;
import org.jnsgaii.functions.OptimizationFunction;
import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.observation.EvolutionObservable;
import org.jnsgaii.observation.EvolutionObserver;
import org.jnsgaii.operators.Operator;
import org.jnsgaii.population.EvaluatedPopulation;
import org.jnsgaii.population.Population;
import org.jnsgaii.population.PopulationData;
import org.jnsgaii.population.PopulationGenerator;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;
import org.jnsgaii.properties.Requirement;

import java.util.*;

/**
 * Created by Mitchell on 11/25/2015.
 */

public class NSGAII<E> implements HasPropertyRequirements, EvolutionObservable<E> {

    private final List<EvolutionObserver<E>> observers;
    private final List<Computation<E, ?>> computations;
    private final List<OptimizationFunction<E>> optimizationFunctions;
    private final Operator<E> operator;
    private final Properties properties;
    private final PopulationGenerator<E> populationGenerator;
    private FrontedPopulation<E> population;
    private int currentGeneration;
    private long previousObservationTime;
    private boolean initialGeneration;
    private Population<E> initialPopulation;

    public NSGAII(Properties properties, Operator<E> operator, List<OptimizationFunction<E>> optimizationFunctions, PopulationGenerator<E> populationGenerator) {
        this(properties, operator, optimizationFunctions, populationGenerator, 1, new ArrayList<>());
    }

    public NSGAII(Properties properties, Operator<E> operator, List<OptimizationFunction<E>> optimizationFunctions, PopulationGenerator<E> populationGenerator, int startGeneration) {
        this(properties, operator, optimizationFunctions, populationGenerator, startGeneration, new ArrayList<>());
    }

    public NSGAII(Properties properties, Operator<E> operator, List<OptimizationFunction<E>> optimizationFunctions, PopulationGenerator<E> populationGenerator, List<Computation<E, ?>> computations) {
        this(properties, operator, optimizationFunctions, populationGenerator, 1, computations);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public NSGAII(Properties properties, Operator<E> operator, List<OptimizationFunction<E>> optimizationFunctions, PopulationGenerator<E> populationGenerator, int startGeneration, List<Computation<E, ?>> computations) {
        if (optimizationFunctions.size() < 1)
            throw new IllegalArgumentException("There must be at least one optimization function!");

        if (!properties.locked())
            properties.lock();

        this.observers = new LinkedList<>();
        this.computations = computations;
        this.optimizationFunctions = new ArrayList<>(optimizationFunctions);
        this.operator = operator;
        this.properties = properties;
        this.populationGenerator = populationGenerator;
        this.currentGeneration = startGeneration;

        this.checkKeyAvailability();

        initialGeneration = true;
    }

    private void checkKeyAvailability() {
        Collection<Key> missingKeys = new LinkedHashSet<>();
        Collection<String> failedRequirements = new LinkedHashSet<>();


        //noinspection SpellCheckingInspection
        Collection<HasPropertyRequirements> hasPropertyRequirementses = new LinkedList<>(Arrays.asList(this.operator, this, this.populationGenerator)); // Hobbitses...
        hasPropertyRequirementses.addAll(this.optimizationFunctions);

        for (HasPropertyRequirements hasPropertyRequirements : hasPropertyRequirementses) {
            for (Key key : hasPropertyRequirements.requestProperties())
                try {
                    this.properties.testKey(key);
                } catch (NoValueSetException e) {
                    missingKeys.add(key);
                }
            for (Requirement requirement : hasPropertyRequirements.requestDetailedRequirements()) {
                boolean result = false;
                try {
                    result = requirement.test(properties);
                } catch (Exception ignored) {
                }
                if (!result) {
                    failedRequirements.add(requirement.describe());
                }
            }
        }

        boolean error = !missingKeys.isEmpty() || !failedRequirements.isEmpty();

        if (error)
            System.err.println("Fatal error!");
        if (!missingKeys.isEmpty()) {
            System.err.println("Missing Keys:");
            for (Key key : missingKeys)
                System.err.println("\t" + key);
        }
        if (!failedRequirements.isEmpty()) {
            System.err.println("Failed Requirements:");
            for (String requirement : failedRequirements)
                System.err.println("\t" + requirement);
        }
        if (error)
            throw new NoValueSetException("Invalid properties!");
    }


    @SuppressWarnings("UnnecessaryLocalVariable")
    public void runGeneration() {
        /*
        DONE!

        The plan:

        In constructor:
        [X] 1. Generate random Population
        [X] 2. Evaluate it into an EvaluatedPopulation
        [X] 3. Turn it into a FrontedPopulation
        [X] 4. Assign the new FrontedPopulation to the instance's population

        In this method:
        [X] 1. Generate offspring
        [X] 2. Merge into a 2x-sized Population
        [X] 3. Evaluate it into an EvaluatedPopulation
        [X] 4. Turn it into a FrontedPopulation
        [X] 5. Cut off the bottom 50% of the FrontedPopulation into a new FrontedPopulation
        [X] 6. Assign the .5x-sized FrontedPopulation to the instance's population

        Everywhere else:
        [X] 1. Finish FrontedPopulation class
        [X] 2. Write Population.merge() method
        [X] 3. Write proper Double classes
         */

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        //System.err.println("Parent size: " + this.population.size());
        if (initialGeneration)
            initialPopulation = new Population<>(2 * this.properties.getInt(Key.IntKey.DefaultIntKey.POPULATION_SIZE), this.populationGenerator, this.properties);

        Population<E> offspring = initialGeneration ?
                null :
                this.operator.apply(this.population, this.properties);
        stopWatch.stop();

        long operatorApplyingTime = stopWatch.getNanoTime();
        stopWatch.reset();

        stopWatch.start();
        //System.err.println("Offspring size: " + offspring.size());
        Population<E> mergedPopulation = initialGeneration ?
                initialPopulation :
                Population.merge(offspring, this.population);
        stopWatch.stop();

        long mergingTime = stopWatch.getNanoTime();
        stopWatch.reset();

        //System.err.println("Merged size: " + merged.size());
        EvaluatedPopulation<E> evaluatedPopulation = new EvaluatedPopulation<>(mergedPopulation, this.optimizationFunctions, this.computations, this.properties);
        long[] computationTimes = evaluatedPopulation.getComputationTimes();
        long[] optimizationFunctionTimes = evaluatedPopulation.getOptimizationFunctionTimes();


        stopWatch.start();
        //System.err.println("Evaluated size: " + evaluatedPopulation.size());
        FrontedPopulation<E> frontedPopulation = new FrontedPopulation<>(evaluatedPopulation, optimizationFunctions, this.properties);
        stopWatch.stop();

        long frontingTime = stopWatch.getNanoTime();
        stopWatch.reset();

        stopWatch.start();
        //System.err.println("Fronted size: " + frontedPopulation.size());
        FrontedPopulation<E> truncatedPopulation = frontedPopulation.truncate(this.properties.getInt(Key.IntKey.DefaultIntKey.POPULATION_SIZE));
        stopWatch.stop();

        long truncationTime = stopWatch.getNanoTime();
        stopWatch.reset();

        //System.err.println("Truncated size: " + truncatedPopulation.size());
        this.population = truncatedPopulation;
        this.currentGeneration++;

        if (initialGeneration) {
            initialGeneration = false;
            initialPopulation = null;
        }

        PopulationData<E> populationData = new PopulationData<>(frontedPopulation, truncatedPopulation, operatorApplyingTime, mergingTime, computationTimes, optimizationFunctionTimes, frontingTime, truncationTime, previousObservationTime, this.currentGeneration);

        stopWatch.start();
        this.update(populationData);
        stopWatch.stop();
        previousObservationTime = stopWatch.getNanoTime();
    }

    private void update(PopulationData<E> populationData) {
        if (properties.getInt(Key.IntKey.DefaultIntKey.OBSERVER_UPDATE_SKIP_NUM) == 1
                || this.currentGeneration % properties.getInt(Key.IntKey.DefaultIntKey.OBSERVER_UPDATE_SKIP_NUM) == 0)
            this.observers.forEach(observer -> observer.update(populationData));
    }

    @Override
    public boolean addObserver(EvolutionObserver<E> observer) {
        return this.observers.add(observer);
    }

    @Override
    @SuppressWarnings("unused")
    public boolean removeObserver(EvolutionObserver<E> observer) {
        return this.observers.remove(observer);
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.IntKey.DefaultIntKey.POPULATION_SIZE,
                Key.BooleanKey.DefaultBooleanKey.THREADED,
                Key.IntKey.DefaultIntKey.OBSERVER_UPDATE_SKIP_NUM
        };
    }

    @Override
    public Requirement[] requestDetailedRequirements() {
        return new Requirement[]{
                new Requirement() {
                    @Override
                    public String describe() {
                        return "Population must be greater than 0";
                    }

                    @Override
                    public boolean test(Properties properties) {
                        return properties.getInt(Key.IntKey.DefaultIntKey.POPULATION_SIZE) > 0;
                    }
                },
                new Requirement() {
                    @Override
                    public String describe() {
                        return "Observer update skip number must be >= 1";
                    }

                    @Override
                    public boolean test(Properties properties) {
                        return properties.getInt(Key.IntKey.DefaultIntKey.OBSERVER_UPDATE_SKIP_NUM) >= 1;
                    }
                }
        };
    }
}
