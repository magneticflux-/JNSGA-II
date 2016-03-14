package org.skaggs.ec.examples.numarical;

import org.skaggs.ec.population.PopulationGenerator;
import org.skaggs.ec.population.individual.Individual;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;
import org.skaggs.ec.properties.Requirement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by skaggsm on 12/27/15.
 */
public class DoubleArrayPopulationGenerator implements PopulationGenerator<double[]> {
    @Override
    public List<Individual<double[]>> generatePopulation(int num, Properties properties) {
        Random r = ThreadLocalRandom.current();
        double min = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM);
        double max = properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM);
        int length = properties.getInt(Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH);
        //double initialMutationStrength = properties.getDouble(Key.DefaultDoubleKey.INITIAL_MUTATION_STRENGTH);
        //double initialMutationProbability = properties.getDouble(Key.DefaultDoubleKey.INITIAL_MUTATION_PROBABILITY);
        //double initialCrossoverStrength = properties.getDouble(Key.DefaultDoubleKey.INITIAL_CROSSOVER_STRENGTH);
        //double initialCrossoverProbability = properties.getDouble(Key.DefaultDoubleKey.INITIAL_CROSSOVER_PROBABILITY);

        double[] initialAspects = (double[]) properties.getValue(Key.DoubleKey.DefaultDoubleKey.INITIAL_ASPECT_ARRAY);

        //int numAspects = properties.getInt(Key.DefaultIntKey.ASPECT_COUNT);
        //double[] aspects = new double[numAspects * 2];
        //aspects[0] = initialCrossoverStrength;
        //aspects[1] = initialCrossoverProbability;
        //aspects[2] = initialMutationStrength;
        //aspects[3] = initialMutationProbability;

        List<Individual<double[]>> individuals = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            individuals.add(new Individual<>(this.getIndividual(r, length, min, max), initialAspects.clone()));
        }
        return individuals;
    }

    private double[] getIndividual(Random r, int length, double min, double max) {
        double[] val = new double[length];
        for (int i = 0; i < val.length; i++) {
            val[i] = this.getRandomDouble(r, min, max);
        }
        return val;
    }

    private double getRandomDouble(Random r, double min, double max) {
        return (r.nextDouble() * (max - min)) + min;
    }

    @Override
    public Key[] requestProperties() {
        return new Key[]{

                Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH,
                /*
                Key.DefaultDoubleKey.INITIAL_MUTATION_STRENGTH, Key.DefaultDoubleKey.INITIAL_MUTATION_PROBABILITY, Key.DefaultDoubleKey.INITIAL_CROSSOVER_STRENGTH, Key.DefaultDoubleKey.INITIAL_CROSSOVER_PROBABILITY,
                */
                Key.IntKey.DefaultIntKey.ASPECT_COUNT
        };
    }

    @Override
    public Requirement[] requestDetailedRequirements() {
        return new Requirement[]{
                new Requirement() {
                    @Override
                    public String describe() {
                        return "Number of aspects must be >= 2 (Array size must be >= 4)";
                    }

                    @Override
                    public boolean test(Properties properties) {
                        return properties.getInt(Key.IntKey.DefaultIntKey.ASPECT_COUNT) >= 2;
                    }
                },
                new Requirement() {
                    @Override
                    public String describe() {
                        return "DefaultDoubleKey \"INITIAL_ASPECT_ARRAY\" must be of type\"double[]\"";
                    }

                    @Override
                    public boolean test(Properties properties) {
                        return properties.getValue(Key.DoubleKey.DefaultDoubleKey.INITIAL_ASPECT_ARRAY) instanceof double[];
                    }
                },
                new Requirement() {
                    @Override
                    public String describe() {
                        return "INITIAL_ASPECT_ARRAY's size must equal ASPECT_COUNT * 2";
                    }

                    @Override
                    public boolean test(Properties properties) {
                        return properties.getInt(Key.IntKey.DefaultIntKey.ASPECT_COUNT) * 2 == ((double[]) properties.getValue(Key.DoubleKey.DefaultDoubleKey.INITIAL_ASPECT_ARRAY)).length;
                    }
                }
        };
    }
}
