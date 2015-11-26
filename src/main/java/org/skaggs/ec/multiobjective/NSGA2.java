package org.skaggs.ec.multiobjective;

import org.skaggs.ec.EvolutionObserver;
import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.exceptions.NoValueSetException;
import org.skaggs.ec.operators.Operator;
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
public class NSGA2<E> {

    private final List<EvolutionObserver<E>> observers;
    private final List<OptimizationFunction<? extends E>> functions;
    private final List<Operator<? extends E>> operators;
    private final PopulationGenerator<? extends E> populationGenerator;
    private final Properties properties;

    public NSGA2(Properties properties, List<Operator<? extends E>> operators, List<OptimizationFunction<? extends E>> functions, PopulationGenerator<? extends E> populationGenerator) {
        if (functions.size() < 1)
            throw new IllegalArgumentException("There must be at least one optimization function!");
        if (operators.size() < 1)
            throw new IllegalArgumentException("There must be at least one operator!");

        if (!properties.locked())
            properties.lock();

        this.observers = new LinkedList<>();
        this.functions = new LinkedList<>(functions);
        this.operators = new LinkedList<>(operators);
        this.populationGenerator = populationGenerator;
        this.properties = properties;

        Collection<Key> missingKeys = new HashSet<>();
        for (HasPropertyRequirements hasPropertyRequirements : operators) {
            for (Key key : hasPropertyRequirements.requestProperties()) {
                try {
                    properties.testKey(key);
                } catch (NoValueSetException e) {
                    missingKeys.add(key);
                }
            }
        }
        if (!missingKeys.isEmpty()) {
            throw new NoValueSetException("These required keys have no default value. Please provide a value. " + missingKeys);
        }
    }

    private void update(PopulationData<E> populationData) {
        this.observers.forEach(observer -> observer.update(populationData));
    }
}
