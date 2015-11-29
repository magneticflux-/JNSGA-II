package org.skaggs.ec.example;

import org.skaggs.ec.population.Individual;
import org.skaggs.ec.population.PopulationGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Mitchell on 11/27/2015.
 */
public class DoublePopulationGenerator implements PopulationGenerator<Double> {
    private final double min;
    private final double max;
    private final double difference;

    public DoublePopulationGenerator(double min, double max) {
        if (max <= min)
            throw new IllegalArgumentException("Maximum must be greater than minimum!");

        this.min = min;
        this.max = max;
        difference = max - min;
    }

    @Override
    public List<Individual<Double>> generatePopulation(int num) {
        Random r = new Random();
        List<Individual<Double>> pop = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            pop.add(new Individual<>((r.nextDouble() * difference) + min));
        }
        return pop;
    }
}
