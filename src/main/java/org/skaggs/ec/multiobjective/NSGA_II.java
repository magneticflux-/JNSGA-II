package org.skaggs.ec.multiobjective;

import org.skaggs.ec.EvolutionObserver;
import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.exceptions.NoValueSetException;
import org.skaggs.ec.multiobjective.population.FrontedPopulation;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.EvaluatedPopulation;
import org.skaggs.ec.population.Population;
import org.skaggs.ec.population.PopulationData;
import org.skaggs.ec.population.PopulationGenerator;
import org.skaggs.ec.properties.HasPropertyRequirements;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class NSGA_II<E> implements HasPropertyRequirements {

    private final List<EvolutionObserver<E>> observers;
    private final List<OptimizationFunction<E>> optimizationFunctions;
    private final Operator<E> operator;
    private final Properties properties;
    private FrontedPopulation<E> population;

    public NSGA_II(Properties properties, Operator<E> operator, List<OptimizationFunction<E>> optimizationFunctions, PopulationGenerator<E> populationGenerator) {
        if (optimizationFunctions.size() < 1)
            throw new IllegalArgumentException("There must be at least one optimization function!");

        if (!properties.locked())
            properties.lock();

        this.observers = new LinkedList<>();
        this.optimizationFunctions = new LinkedList<>(optimizationFunctions);
        this.operator = operator;
        this.properties = properties;

        this.checkKeyAvailability();

        Population<E> initialPopulation = new Population<>(2 * properties.getInt(Key.IntKey.INT_POPULATION), populationGenerator);
        EvaluatedPopulation<E> evaluatedPopulation = new EvaluatedPopulation<>(initialPopulation, optimizationFunctions, properties.getBoolean(Key.BooleanKey.BOOLEAN_THREADED));
        //noinspection UnnecessaryLocalVariable
        FrontedPopulation<E> frontedPopulation = new FrontedPopulation<>(evaluatedPopulation);
        this.population = frontedPopulation;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public void runGeneration() {
        /*
        TODO Check all da boxes!

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
        [ ] 1. Finish FrontedPopulation class
        [ ] 2. Write Population.merge() method
        [ ] 3.
         */

        Population<E> offspring = this.operator.apply(this.population, this.properties);
        Population<E> merged = Population.merge(this.population, offspring);
        EvaluatedPopulation<E> evaluatedPopulation = new EvaluatedPopulation<>(merged, this.optimizationFunctions, this.properties.getBoolean(Key.BooleanKey.BOOLEAN_THREADED));
        FrontedPopulation<E> frontedPopulation = new FrontedPopulation<>(evaluatedPopulation);
        FrontedPopulation<E> truncatedPopulation = frontedPopulation.truncate(this.properties.getInt(Key.IntKey.INT_POPULATION));
        this.population = truncatedPopulation;
        this.update(new PopulationData<>(this.population));
    }

    private void checkKeyAvailability() {
        Collection<Key> missingKeys = new HashSet<>();

        HasPropertyRequirements[] hasPropertyRequirementses = new HasPropertyRequirements[]{this.operator, this};

        for (HasPropertyRequirements hasPropertyRequirements : hasPropertyRequirementses)
            for (Key key : hasPropertyRequirements.requestProperties())
                try {
                    this.properties.testKey(key);
                } catch (NoValueSetException e) {
                    missingKeys.add(key);
                }

        if (!missingKeys.isEmpty()) {
            throw new NoValueSetException("These required keys have no default value. Please provide a value. " + missingKeys);
        }
    }

    private void update(PopulationData<E> populationData) {
        this.observers.forEach(observer -> observer.update(populationData));
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.IntKey.INT_POPULATION,
                Key.DoubleKey.DOUBLE_ELITE_FRACTION,
                Key.BooleanKey.BOOLEAN_THREADED
        };
    }
}
