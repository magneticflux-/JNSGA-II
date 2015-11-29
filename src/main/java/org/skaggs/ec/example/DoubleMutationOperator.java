package org.skaggs.ec.example;

import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.Individual;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by Mitchell on 11/27/2015.
 */
public class DoubleMutationOperator implements Operator<Double> {

    private final Random random;
    private final double n;

    public DoubleMutationOperator(Random random, double n) {
        this.random = random;
        this.n = n;
    }

    private Individual<Double> mutate(Individual<Double> d) {
        return new Individual<>(d.getIndividual() + random.nextDouble() - .5);
    }

    @Override
    public Key[] requestProperties() {
        return new Key[0];
    }

    @Override
    public List<Individual<Double>> apply(List<Individual<Double>> population, Properties properties) {
        return population.parallelStream().map(this::mutate).collect(Collectors.toList());
    }
}
