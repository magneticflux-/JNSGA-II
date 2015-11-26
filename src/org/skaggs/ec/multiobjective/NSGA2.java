package org.skaggs.ec.multiobjective;

import org.skaggs.ec.EvolutionObserver;
import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.PopulationGenerator;
import org.skaggs.ec.properties.Properties;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mitchell on 11/25/2015.
 */
public class NSGA2<E> {
    private final List<EvolutionObserver> observers;
    private final List<OptimizationFunction<? extends E>> functions;
    private final List<Operator<? extends E>> operators;
    private final PopulationGenerator<? extends E> populationGenerator;

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
    }

    private void update() {
        observers.forEach(EvolutionObserver::update);
    }
}
