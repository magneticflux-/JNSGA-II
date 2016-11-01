package org.jnsgaii.examples.numarical;

import org.jnsgaii.multiobjective.population.FrontedIndividual;
import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.operators.Operator;
import org.jnsgaii.population.Population;
import org.jnsgaii.population.individual.Individual;
import org.jnsgaii.properties.HasPropertyRequirements;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by skaggsm on 12/27/15.
 *
 * @author Mitchell Skaggs
 * @see Operator
 * @see HasPropertyRequirements
 */
public class SimpleDoubleMutationOperator implements Operator<Double> {
    @Override
    public Population<Double> apply(FrontedPopulation<Double> population, Properties properties) {
        Random r = new Random();
        double probability = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.INITIAL_MUTATION_PROBABILITY);
        double range = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.INITIAL_MUTATION_STRENGTH);

        List<Individual<Double>> individuals = new ArrayList<>(population.getPopulation().size());

        for (FrontedIndividual<Double> d : population.getPopulation()) {
            if (r.nextDouble() < probability)
                individuals.add(new Individual<>(this.mutate(d.getIndividual(), r, range)));
            else
                individuals.add(new Individual<>(d.getIndividual()));
        }

        return new Population<>(individuals, -1);
    }

    private double mutate(double d, Random r, double range) {
        return (d + (r.nextDouble() * 2 * range)) - range;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{Key.DoubleKey.DefaultDoubleKey.INITIAL_MUTATION_PROBABILITY, Key.DoubleKey.DefaultDoubleKey.INITIAL_MUTATION_STRENGTH};
    }
}
