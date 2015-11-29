package org.skaggs.ec.multiobjective;

import org.skaggs.ec.EvolutionObserver;
import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.exceptions.NoValueSetException;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.Individual;
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
    private final List<OptimizationFunction<E>> functions;
    private final Operator<E> operator;
    private final PopulationGenerator<E> populationGenerator;
    private final Properties properties;
    private Population<E> population;

    public NSGA_II(Properties properties, Operator<E> operator, List<OptimizationFunction<E>> functions, PopulationGenerator<E> populationGenerator) {
        if (functions.size() < 1)
            throw new IllegalArgumentException("There must be at least one optimization function!");

        if (!properties.locked())
            properties.lock();

        this.observers = new LinkedList<>();
        this.functions = new LinkedList<>(functions);
        this.operator = operator;
        this.populationGenerator = populationGenerator;
        this.properties = properties;

        checkKeyAvailability();

        population = new Population<>(properties.getInt(Key.IntKey.INT_POPULATION), populationGenerator);
    }

    public void runGeneration() {
        Population<E> children;
        List<Individual<E>> newIndividuals = operator.apply(population.getPopulation(), properties);
        //TODO Implement rest of generation cycle
    }

    private void checkKeyAvailability() {
        Collection<Key> missingKeys = new HashSet<>();

        HasPropertyRequirements[] hasPropertyRequirementses = new HasPropertyRequirements[]{operator, this};

        for (HasPropertyRequirements hasPropertyRequirements : hasPropertyRequirementses)
            for (Key key : hasPropertyRequirements.requestProperties())
                try {
                    properties.testKey(key);
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
