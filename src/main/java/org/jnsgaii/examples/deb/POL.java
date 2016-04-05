package org.jnsgaii.examples.deb;

import org.apache.commons.math3.util.FastMath;
import org.jnsgaii.DefaultOptimizationFunction;
import org.jnsgaii.OptimizationFunction;
import org.jnsgaii.examples.defaultoperatorframework.DoubleArrayAverageRecombiner;
import org.jnsgaii.examples.defaultoperatorframework.DoubleArraySpeciator;
import org.jnsgaii.examples.defaultoperatorframework.RouletteWheelLinearSelection;
import org.jnsgaii.examples.numarical.DoubleArrayPopulationGenerator;
import org.jnsgaii.multiobjective.NSGA_II;
import org.jnsgaii.operators.DefaultOperator;
import org.jnsgaii.operators.Mutator;
import org.jnsgaii.population.PopulationGenerator;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;
import org.jnsgaii.visualization.DefaultVisualization;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by skaggsm on 12/27/15.
 */
public final class POL {
    private POL() {
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        //noinspection MagicNumber
        Properties properties = new Properties()
                .setBoolean(Key.BooleanKey.DefaultBooleanKey.THREADED, true)
                .setInt(Key.IntKey.DefaultIntKey.OBSERVER_UPDATE_SKIP_NUM, 5)
                .setInt(Key.IntKey.DefaultIntKey.POPULATION_SIZE, 1000)
                .setDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, -10)//-FastMath.PI)
                .setDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, 10)//FastMath.PI)
                .setInt(Key.IntKey.DefaultIntKey.DOUBLE_ARRAY_GENERATION_LENGTH, 3)

                .setValue(Key.DoubleKey.DefaultDoubleKey.INITIAL_ASPECT_ARRAY, new double[]{
                        0, 1, // Crossover STR/PROB
                        .25,   // Speciator DISTANCE
                        0, 1, // Mutation 1 STR/PROB
                        1, 0  // Mutation 2
                })
                .setValue(Key.DoubleKey.DefaultDoubleKey.ASPECT_MODIFICATION_ARRAY, new double[]{
                        .125 / 4, 1, // Crossover STR
                        .125 / 16, 1, // Crossover PROB
                        .125 / 4, 1, // Speciator DISTANCE
                        .125 / 16, 1, // Mutation 1 STR
                        .125 / 16, 1, // Mutation 1 PROB
                        .125 / 16, 1, // Mutation 2 STR
                        .125 / 16, 1, // Mutation 2 PROB
                });

        DefaultOperator<double[]> operator = new DefaultOperator<>(Arrays.asList(
                new Mutator<double[]>() {
                    @Override
                    protected double[] mutate(double[] object, double mutationStrength, double mutationProbability) {
                        Random r = ThreadLocalRandom.current();
                        double[] newObject = new double[object.length];
                        for (int i = 0; i < newObject.length; i++) {
                            if (r.nextDouble() < mutationProbability) {
                                newObject[i] = (r.nextGaussian() * mutationStrength) + object[i];
                            }
                            //newObject[i] = Mutator.mutate(object[i], r, mutationStrength);
                        }
                        return newObject;
                    }
                },
                new Mutator<double[]>() {
                    @Override
                    protected double[] mutate(double[] object, double mutationStrength, double mutationProbability) {
                        Random r = ThreadLocalRandom.current();
                        double[] newObject = new double[object.length];
                        for (int i = 0; i < newObject.length; i++) {
                            if (r.nextDouble() < mutationProbability) {
                                newObject[i] = Mutator.mutate(object[i], r, mutationStrength);
                            }
                        }
                        return newObject;
                    }
                }
        ), new DoubleArrayAverageRecombiner(), new RouletteWheelLinearSelection<>(), new DoubleArraySpeciator());
        OptimizationFunction<double[]> function1 = new Function1();
        OptimizationFunction<double[]> function2 = new Function2();
        OptimizationFunction<double[]> function3 = new Function3();
        OptimizationFunction<double[]> function4 = new Function4();

        List<OptimizationFunction<double[]>> optimizationFunctions = Arrays.asList(function1, function2, function3, function4);
        PopulationGenerator<double[]> populationGenerator = new DoubleArrayPopulationGenerator();

        NSGA_II<double[]> nsga_ii = new NSGA_II<>(properties, operator, optimizationFunctions, populationGenerator);

        DefaultVisualization.startInterface(operator, optimizationFunctions, nsga_ii, properties);

        //noinspection MagicNumber
        for (int i = 0; i < 1000000; i++) {
            //long startTime = System.nanoTime();
            nsga_ii.runGeneration();
            //long elapsedTime = System.nanoTime() - startTime;
            //System.out.println("; Total elapsed time: " + String.format("%.4f", elapsedTime / 1000000d) + "ms");
            //noinspection MagicNumber
            //Thread.sleep(200);
        }
    }

    private static class Function1 extends DefaultOptimizationFunction<double[]> {
        @SuppressWarnings("MagicNumber")
        @Override
        public double evaluateIndividual(double[] vector, Properties properties) {
            assert vector.length == 2;
            final double A1 = (((.5 * FastMath.sin(1)) - (2 * FastMath.cos(1))) + FastMath.sin(2)) - (1.5 * FastMath.cos(2));
            final double A2 = (((1.5 * FastMath.sin(1)) - FastMath.cos(1)) + (2 * FastMath.sin(2))) - (.5 * FastMath.cos(2));
            final double B1 = (((.5 * FastMath.sin(vector[0])) - (2 * FastMath.cos(vector[0]))) + FastMath.sin(vector[1])) - (1.5 * FastMath.cos(vector[1]));
            final double B2 = (((1.5 * FastMath.sin(vector[0])) - FastMath.cos(vector[0])) + (2 * FastMath.sin(vector[1]))) - (.5 * FastMath.cos(vector[1]));
            return 1 + FastMath.pow(A1 - B1, 2) + FastMath.pow(A2 - B2, 2);
        }

        @Override
        public int compare(Double o1, Double o2) {
            return -Double.compare(o1, o2);
        }

        @Override
        public double min(Properties properties) {
            return 0;
        }

        @Override
        public Key[] requestProperties() {
            return new Key[0];
        }


        @SuppressWarnings("MagicNumber")
        @Override
        public double max(Properties properties) {
            return 30;
        }


    }

    private static class Function2 extends DefaultOptimizationFunction<double[]> {
        @Override
        public double evaluateIndividual(double[] object, Properties properties) {
            assert object.length == 2;
            return FastMath.pow(object[0] + 3, 2) + FastMath.pow(object[1] + 1, 2);
        }

        @Override
        public double min(Properties properties) {
            return 0;
        }

        @Override
        public double max(Properties properties) {
            return FastMath.pow(FastMath.PI + 3, 2) + FastMath.pow(FastMath.PI + 1, 2);
        }

        @Override
        public int compare(Double o1, Double o2) {
            return -Double.compare(o1, o2);
        }

        @Override
        public Key[] requestProperties() {
            return new Key[0];
        }
    }

    private static class Function3 extends DefaultOptimizationFunction<double[]> {
        @Override
        public double evaluateIndividual(double[] object, Properties properties) {
            assert object.length == 3;
            double result = 0;
            for (int i = 0; i < object.length - 1; i++) {
                result += -10 * FastMath.exp(-0.2 * FastMath.sqrt(FastMath.pow(object[i], 2) + FastMath.pow(object[i + 1], 2)));
            }
            return result;
        }

        @Override
        public double min(Properties properties) {
            return 0;
        }

        @Override
        public double max(Properties properties) {
            return 1;
        }

        @Override
        public int compare(Double o1, Double o2) {
            return -Double.compare(o1, o2);
        }

        @Override
        public Key[] requestProperties() {
            return new Key[0];
        }
    }

    private static class Function4 extends DefaultOptimizationFunction<double[]> {
        @Override
        public double evaluateIndividual(double[] object, Properties properties) {
            assert object.length == 3;
            double result = 0;
            for (int i = 0; i < object.length; i++) {
                result += FastMath.pow(FastMath.abs(object[i]), 0.8) + 5 * FastMath.sin(FastMath.pow(object[i], 3));
            }
            return result;
        }

        @Override
        public double min(Properties properties) {
            return 0;
        }

        @Override
        public double max(Properties properties) {
            return 1;
        }

        @Override
        public int compare(Double o1, Double o2) {
            return -Double.compare(o1, o2);
        }

        @Override
        public Key[] requestProperties() {
            return new Key[0];
        }
    }
}
