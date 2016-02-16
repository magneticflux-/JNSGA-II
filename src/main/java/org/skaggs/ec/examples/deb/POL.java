package org.skaggs.ec.examples.deb;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.util.FastMath;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.examples.defaultoperatorframework.DoubleArrayAverageCrossoverer;
import org.skaggs.ec.examples.defaultoperatorframework.DoubleArrayMutator;
import org.skaggs.ec.examples.defaultoperatorframework.DoubleArraySpeciator;
import org.skaggs.ec.examples.defaultoperatorframework.RouletteWheelLinearSelection;
import org.skaggs.ec.examples.numarical.DoubleArrayPopulationGenerator;
import org.skaggs.ec.multiobjective.NSGA_II;
import org.skaggs.ec.multiobjective.population.Front;
import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.operators.DefaultOperator;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.PopulationGenerator;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.lang.reflect.InvocationTargetException;
import java.text.AttributedString;

/**
 * Created by skaggsm on 12/27/15.
 */
public final class POL {
    private POL() {
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        //Thread.sleep(5000);
        XYSeriesCollection currentGenerationCollection = new XYSeriesCollection();
        JFreeChart currentGenerationChart = ChartFactory.createScatterPlot("Functions", "Function 1", "Function 2", currentGenerationCollection, PlotOrientation.VERTICAL, true, false, false);
        currentGenerationChart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
        ChartPanel currentGenerationPanel = new ChartPanel(currentGenerationChart);

        XYSeriesCollection currentPopulationCollection = new XYSeriesCollection();
        JFreeChart currentPopulationChart = ChartFactory.createScatterPlot("Individuals", "", "", currentPopulationCollection, PlotOrientation.VERTICAL, true, false, false);
        currentPopulationChart.getXYPlot().getDomainAxis().setAttributedLabel(new AttributedString("X\u2081"));
        currentPopulationChart.getXYPlot().getRangeAxis().setAttributedLabel(new AttributedString("X\u2082"));
        ChartPanel currentPopulationPanel = new ChartPanel(currentPopulationChart);

        XYSeriesCollection averageMutationStrengthCollection = new XYSeriesCollection();
        XYSeries averageMutationStrength = new XYSeries("Average Mutation Strength");
        averageMutationStrengthCollection.addSeries(averageMutationStrength);
        JFreeChart averageMutationStrengthChart = ChartFactory.createScatterPlot("Average Mutation Strength", "Generation", "Y", averageMutationStrengthCollection, PlotOrientation.VERTICAL, true, false, false);
        averageMutationStrengthChart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
        ChartPanel averageMutationStrengthPanel = new ChartPanel(averageMutationStrengthChart);

        XYSeriesCollection averageMutationProbabilityCollection = new XYSeriesCollection();
        XYSeries averageMutationProbability = new XYSeries("Average Mutation Probability");
        averageMutationProbabilityCollection.addSeries(averageMutationProbability);
        JFreeChart averageMutationProbabilityChart = ChartFactory.createScatterPlot("Average Mutation Probability", "Generation", "Y", averageMutationProbabilityCollection, PlotOrientation.VERTICAL, true, false, false);
        averageMutationProbabilityChart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
        ChartPanel averageMutationProbabilityPanel = new ChartPanel(averageMutationProbabilityChart);

        XYSeriesCollection averageCrossoverStrengthCollection = new XYSeriesCollection();
        XYSeries averageCrossoverStrength = new XYSeries("Average Crossover Strength");
        averageCrossoverStrengthCollection.addSeries(averageCrossoverStrength);
        JFreeChart averageCrossoverStrengthChart = ChartFactory.createScatterPlot("Average Crossover Strength", "Generation", "Y", averageCrossoverStrengthCollection, PlotOrientation.VERTICAL, true, false, false);
        averageCrossoverStrengthChart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
        ChartPanel averageCrossoverStrengthPanel = new ChartPanel(averageCrossoverStrengthChart);

        XYSeriesCollection averageCrossoverProbabilityCollection = new XYSeriesCollection();
        XYSeries averageCrossoverProbability = new XYSeries("Average Crossover Probability");
        averageCrossoverProbabilityCollection.addSeries(averageCrossoverProbability);
        JFreeChart averageCrossoverProbabilityChart = ChartFactory.createScatterPlot("Average Crossover Probability", "Generation", "Y", averageCrossoverProbabilityCollection, PlotOrientation.VERTICAL, true, false, false);
        averageCrossoverProbabilityChart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
        ChartPanel averageCrossoverProbabilityPanel = new ChartPanel(averageCrossoverProbabilityChart);

        JFrame frame = new JFrame("Evolutionary Algorithm");
        frame.setLayout(new GridLayout(3, 2));
        frame.add(currentGenerationPanel);
        frame.add(currentPopulationPanel);
        frame.add(averageMutationStrengthPanel);
        frame.add(averageMutationProbabilityPanel);
        frame.add(averageCrossoverStrengthPanel);
        frame.add(averageCrossoverProbabilityPanel);
        //noinspection MagicNumber
        frame.setSize(1400, 1000);
        //noinspection MagicNumber
        frame.setLocation(400, 0);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //noinspection MagicNumber
        Properties properties = new Properties()
                .setBoolean(Key.BooleanKey.THREADED, true)
                .setInt(Key.IntKey.POPULATION_SIZE, 2000)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, -5)//-FastMath.PI)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, 5)//FastMath.PI)
                .setInt(Key.IntKey.DOUBLE_ARRAY_GENERATION_LENGTH, 3)
                .setDouble(Key.DoubleKey.DOUBLE_SPECIATION_MAX_DISTANCE, .5)

                .setDouble(Key.DoubleKey.INITIAL_MUTATION_STRENGTH, .3)
                .setDouble(Key.DoubleKey.INITIAL_MUTATION_PROBABILITY, .5)

                .setDouble(Key.DoubleKey.MUTATION_STRENGTH_MUTATION_STRENGTH, .125 / 32)
                .setDouble(Key.DoubleKey.MUTATION_STRENGTH_MUTATION_PROBABILITY, 1)

                .setDouble(Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_STRENGTH, .125 / 8)
                .setDouble(Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_PROBABILITY, 1)


                .setDouble(Key.DoubleKey.INITIAL_CROSSOVER_STRENGTH, .12)
                .setDouble(Key.DoubleKey.INITIAL_CROSSOVER_PROBABILITY, .99)

                .setDouble(Key.DoubleKey.CROSSOVER_STRENGTH_MUTATION_STRENGTH, .125 / 32)
                .setDouble(Key.DoubleKey.CROSSOVER_STRENGTH_MUTATION_PROBABILITY, 1)

                .setDouble(Key.DoubleKey.CROSSOVER_PROBABILITY_MUTATION_STRENGTH, .125 / 8)
                .setDouble(Key.DoubleKey.CROSSOVER_PROBABILITY_MUTATION_PROBABILITY, 1);

        Operator<double[]> operator = new DefaultOperator<>(new DoubleArrayMutator(), new DoubleArrayAverageCrossoverer(), new RouletteWheelLinearSelection<>(), new DoubleArraySpeciator());
        OptimizationFunction<double[]> function1 = new Function1();
        OptimizationFunction<double[]> function2 = new Function2();
        OptimizationFunction<double[]> function3 = new Function3();
        OptimizationFunction<double[]> function4 = new Function4();
        @SuppressWarnings("unchecked")
        OptimizationFunction<double[]>[] optimizationFunctions = new OptimizationFunction[]{function1, function2, function3};
        PopulationGenerator<double[]> populationGenerator = new DoubleArrayPopulationGenerator();

        NSGA_II<double[]> nsga_ii = new NSGA_II<>(properties, operator, optimizationFunctions, populationGenerator);

        nsga_ii.addObserver(populationData -> {
            currentGenerationChart.setNotify(false);
            currentGenerationCollection.removeAllSeries();
            for (Front<double[]> front : populationData.getFrontedPopulation().getFronts()) {
                    XYSeries frontSeries = new XYSeries(front.toString());
                    for (FrontedIndividual<double[]> individual : front.getMembers()) {
                        frontSeries.add(individual.getScore(0), individual.getScore(1));
                    }
                currentGenerationCollection.addSeries(frontSeries);
            }
            currentGenerationChart.setNotify(true);
        });

        nsga_ii.addObserver(populationData -> {
            currentPopulationChart.setNotify(false);
            currentPopulationCollection.removeAllSeries();
            for (Front<double[]> front : populationData.getFrontedPopulation().getFronts()) {
                XYSeries frontSeries = new XYSeries(front.toString());
                for (FrontedIndividual<double[]> individual : front.getMembers()) {
                    frontSeries.add(individual.getIndividual()[0], individual.getIndividual()[1]);
                }
                currentPopulationCollection.addSeries(frontSeries);
            }
            currentPopulationChart.setNotify(true);
        });

        nsga_ii.addObserver(populationData -> {
            float ms = (populationData.getElapsedTime() / 1000000f);
            System.out.println("Elapsed time in generation " + populationData.getCurrentGeneration() + ": " + ms + "ms;\t ms/front: " + (ms / populationData.getFrontedPopulation().getFronts().size()) + "\t ms/individual: " + (ms / populationData.getFrontedPopulation().getPopulation().size()));
            StatisticalSummary currentMutationStrength = new DescriptiveStatistics(populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.mutationStrength).toArray());
            StatisticalSummary currentMutationProbability = new DescriptiveStatistics(populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.mutationProbability).toArray());
            StatisticalSummary currentCrossoverStrength = new DescriptiveStatistics(populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.crossoverStrength).toArray());
            StatisticalSummary currentCrossoverProbability = new DescriptiveStatistics(populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.crossoverProbability).toArray());
            averageMutationStrength.add(populationData.getCurrentGeneration(), currentMutationStrength.getMean());
            averageMutationProbability.add(populationData.getCurrentGeneration(), currentMutationProbability.getMean());
            averageCrossoverStrength.add(populationData.getCurrentGeneration(), currentCrossoverStrength.getMean());
            averageCrossoverProbability.add(populationData.getCurrentGeneration(), currentCrossoverProbability.getMean());
        });


        //noinspection MagicNumber
        for (int i = 0; i < 1000000; i++) {
            EventQueue.invokeAndWait(nsga_ii::runGeneration);
            //noinspection MagicNumber
            //Thread.sleep(200);
        }
    }

    static class Function1 implements OptimizationFunction<double[]> {
        @SuppressWarnings("MagicNumber")
        @Override
        public double evaluate(double[] vector, Properties properties) {
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
        public Key[] requestProperties() {
            return new Key[0];
        }

        @Override
        public double min(Properties properties) {
            return 0;
        }


        @SuppressWarnings("MagicNumber")
        @Override
        public double max(Properties properties) {
            return 30;
        }


    }

    static class Function2 implements OptimizationFunction<double[]> {
        @Override
        public double evaluate(double[] object, Properties properties) {
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

    static class Function3 implements OptimizationFunction<double[]> {
        @Override
        public double evaluate(double[] object, Properties properties) {
            assert object.length == 3;
            double result = 0;
            for (int i = 0; i < 2; i++) {
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

    static class Function4 implements OptimizationFunction<double[]> {
        @Override
        public double evaluate(double[] object, Properties properties) {
            assert object.length == 3;
            double result = 0;
            for (int i = 0; i < 3; i++) {
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
