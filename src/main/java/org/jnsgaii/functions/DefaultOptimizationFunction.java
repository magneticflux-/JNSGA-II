package org.jnsgaii.functions;

import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Created by Mitchell Skaggs on 2/21/2016.
 */
public abstract class DefaultOptimizationFunction<E> implements OptimizationFunction<E> {
    public abstract double evaluateIndividual(E object, HashMap<String, Object> computationResults, Properties properties);

    @Override
    public double[] evaluate(List<Individual<E>> individuals, HashMap<String, Object>[] computationResults, Properties properties) {
        double[] scores = new double[individuals.size()];

        IntStream stream = IntStream.range(0, scores.length);
        if (properties.getBoolean(Key.BooleanKey.DefaultBooleanKey.THREADED))
            stream = stream.parallel();

        stream.forEach(
                value -> scores[value] = evaluateIndividual(individuals.get(value).getIndividual(), computationResults[value], properties)
        );
        return scores;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{
                Key.BooleanKey.DefaultBooleanKey.THREADED
        };
    }
}
