package org.skaggs.ec.examples.deb;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.StatisticalSummary;
import org.apache.commons.math3.stat.descriptive.rank.Median;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.skaggs.ec.DefaultOptimizationFunction;
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

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
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

        XYErrorRenderer averagePlotRenderer = new XYErrorRenderer();
        averagePlotRenderer.setErrorPaint(Color.PINK);

        XYSeriesCollection currentGenerationCollection = new XYSeriesCollection();
        JFreeChart currentGenerationChart = ChartFactory.createScatterPlot("Functions", "Function 1", "Function 2", currentGenerationCollection, PlotOrientation.VERTICAL, true, false, false);
        currentGenerationChart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
        ChartPanel currentGenerationPanel = new ChartPanel(currentGenerationChart);

        XYSeriesCollection currentPopulationCollection = new XYSeriesCollection();
        JFreeChart currentPopulationChart = ChartFactory.createScatterPlot("Individuals", "", "", currentPopulationCollection, PlotOrientation.VERTICAL, true, false, false);
        currentPopulationChart.getXYPlot().getDomainAxis().setAttributedLabel(new AttributedString("X\u2081"));
        currentPopulationChart.getXYPlot().getRangeAxis().setAttributedLabel(new AttributedString("X\u2082"));
        ChartPanel currentPopulationPanel = new ChartPanel(currentPopulationChart);

        YIntervalSeriesCollection averageMutationStrengthCollection = new YIntervalSeriesCollection();
        YIntervalSeries averageMutationStrength = new YIntervalSeries("Average Mutation Strength");
        YIntervalSeries medianMutationStrength = new YIntervalSeries("Median Mutation Strength");
        averageMutationStrengthCollection.addSeries(averageMutationStrength);
        averageMutationStrengthCollection.addSeries(medianMutationStrength);
        JFreeChart averageMutationStrengthChart = ChartFactory.createScatterPlot("Average Mutation Strength", "Generation", "Y", averageMutationStrengthCollection, PlotOrientation.VERTICAL, true, false, false);
        averageMutationStrengthChart.getXYPlot().setRenderer(averagePlotRenderer);
        ChartPanel averageMutationStrengthPanel = new ChartPanel(averageMutationStrengthChart);

        YIntervalSeriesCollection averageMutationProbabilityCollection = new YIntervalSeriesCollection();
        YIntervalSeries averageMutationProbability = new YIntervalSeries("Average Mutation Probability");
        YIntervalSeries medianMutationProbability = new YIntervalSeries("Median Mutation Probability");
        averageMutationProbabilityCollection.addSeries(averageMutationProbability);
        averageMutationProbabilityCollection.addSeries(medianMutationProbability);
        JFreeChart averageMutationProbabilityChart = ChartFactory.createScatterPlot("Average Mutation Probability", "Generation", "Y", averageMutationProbabilityCollection, PlotOrientation.VERTICAL, true, false, false);
        averageMutationProbabilityChart.getXYPlot().setRenderer(averagePlotRenderer);
        ChartPanel averageMutationProbabilityPanel = new ChartPanel(averageMutationProbabilityChart);

        YIntervalSeriesCollection averageCrossoverStrengthCollection = new YIntervalSeriesCollection();
        YIntervalSeries averageCrossoverStrength = new YIntervalSeries("Average Crossover Strength");
        YIntervalSeries medianCrossoverStrength = new YIntervalSeries("Median Crossover Strength");
        averageCrossoverStrengthCollection.addSeries(averageCrossoverStrength);
        averageCrossoverStrengthCollection.addSeries(medianCrossoverStrength);
        JFreeChart averageCrossoverStrengthChart = ChartFactory.createScatterPlot("Average Crossover Strength", "Generation", "Y", averageCrossoverStrengthCollection, PlotOrientation.VERTICAL, true, false, false);
        averageCrossoverStrengthChart.getXYPlot().setRenderer(averagePlotRenderer);
        ChartPanel averageCrossoverStrengthPanel = new ChartPanel(averageCrossoverStrengthChart);

        YIntervalSeriesCollection averageCrossoverProbabilityCollection = new YIntervalSeriesCollection();
        YIntervalSeries averageCrossoverProbability = new YIntervalSeries("Average Crossover Probability");
        YIntervalSeries medianCrossoverProbability = new YIntervalSeries("Median Crossover Probability");
        averageCrossoverProbabilityCollection.addSeries(averageCrossoverProbability);
        averageCrossoverProbabilityCollection.addSeries(medianCrossoverProbability);
        JFreeChart averageCrossoverProbabilityChart = ChartFactory.createScatterPlot("Average Crossover Probability", "Generation", "Y", averageCrossoverProbabilityCollection, PlotOrientation.VERTICAL, true, false, false);
        averageCrossoverProbabilityChart.getXYPlot().setRenderer(averagePlotRenderer);
        ChartPanel averageCrossoverProbabilityPanel = new ChartPanel(averageCrossoverProbabilityChart);

        JFrame windowFrame = new JFrame("Evolutionary Algorithm");
        JPanel mainPanel = new JPanel();
        windowFrame.setLayout(new BorderLayout());
        windowFrame.add(mainPanel, BorderLayout.CENTER);

        GroupLayout groupLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(currentGenerationPanel)
                        .addComponent(currentPopulationPanel))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(averageMutationStrengthPanel)
                        .addComponent(averageMutationProbabilityPanel))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(averageCrossoverStrengthPanel)
                        .addComponent(averageCrossoverProbabilityPanel))
        );
        groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(currentGenerationPanel)
                        .addComponent(averageMutationStrengthPanel)
                        .addComponent(averageCrossoverStrengthPanel))
                .addGroup(groupLayout.createParallelGroup()
                        .addComponent(currentPopulationPanel)
                        .addComponent(averageMutationProbabilityPanel)
                        .addComponent(averageCrossoverProbabilityPanel))
        );

        //noinspection MagicNumber
        windowFrame.setSize(1400, 1000);
        //noinspection MagicNumber
        windowFrame.setLocation(400, 0);
        windowFrame.setVisible(true);
        windowFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //noinspection MagicNumber
        Properties properties = new Properties()
                .setBoolean(Key.BooleanKey.THREADED, true)
                .setInt(Key.IntKey.POPULATION_SIZE, 1000)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, -2)//-FastMath.PI)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, 2)//FastMath.PI)
                .setInt(Key.IntKey.DOUBLE_ARRAY_GENERATION_LENGTH, 2)
                .setDouble(Key.DoubleKey.DOUBLE_SPECIATION_MAX_DISTANCE, .5)

                .setDouble(Key.DoubleKey.INITIAL_MUTATION_STRENGTH, .1)
                .setDouble(Key.DoubleKey.INITIAL_MUTATION_PROBABILITY, 1)

                .setDouble(Key.DoubleKey.MUTATION_STRENGTH_MUTATION_STRENGTH, .125 / 16)
                .setDouble(Key.DoubleKey.MUTATION_STRENGTH_MUTATION_PROBABILITY, 1)

                .setDouble(Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_STRENGTH, .125 / 16)
                .setDouble(Key.DoubleKey.MUTATION_PROBABILITY_MUTATION_PROBABILITY, 1)


                .setDouble(Key.DoubleKey.INITIAL_CROSSOVER_STRENGTH, 0)
                .setDouble(Key.DoubleKey.INITIAL_CROSSOVER_PROBABILITY, 1)

                .setDouble(Key.DoubleKey.CROSSOVER_STRENGTH_MUTATION_STRENGTH, .125 / 16)
                .setDouble(Key.DoubleKey.CROSSOVER_STRENGTH_MUTATION_PROBABILITY, 1)

                .setDouble(Key.DoubleKey.CROSSOVER_PROBABILITY_MUTATION_STRENGTH, .125 / 16)
                .setDouble(Key.DoubleKey.CROSSOVER_PROBABILITY_MUTATION_PROBABILITY, 1);

        Operator<double[]> operator = new DefaultOperator<>(new DoubleArrayMutator(), new DoubleArrayAverageCrossoverer(), new RouletteWheelLinearSelection<>(), new DoubleArraySpeciator());
        OptimizationFunction<double[]> function1 = new Function1();
        OptimizationFunction<double[]> function2 = new Function2();
        OptimizationFunction<double[]> function3 = new Function3();
        OptimizationFunction<double[]> function4 = new Function4();
        @SuppressWarnings("unchecked")
        OptimizationFunction<double[]>[] optimizationFunctions = new OptimizationFunction[]{function1, function2};
        PopulationGenerator<double[]> populationGenerator = new DoubleArrayPopulationGenerator();

        NSGA_II<double[]> nsga_ii = new NSGA_II<>(properties, operator, optimizationFunctions, populationGenerator);

        nsga_ii.addObserver(populationData -> {
            currentGenerationChart.setNotify(false);
            currentGenerationCollection.removeAllSeries();
            for (Front<double[]> front : populationData.getTruncatedPopulation().getFronts()) {
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
            for (Front<double[]> front : populationData.getTruncatedPopulation().getFronts()) {
                XYSeries frontSeries = new XYSeries(front.toString());
                for (FrontedIndividual<double[]> individual : front.getMembers()) {
                    frontSeries.add(individual.getIndividual()[0], individual.getIndividual()[1]);
                }
                currentPopulationCollection.addSeries(frontSeries);
            }
            currentPopulationChart.setNotify(true);
        });

        nsga_ii.addObserver(populationData -> {
            double elapsedTimeMS = (populationData.getElapsedTime() / 1000000d);
            double observationTimeMS = (populationData.getPreviousObservationTime() / 1000000d);
            System.out.print("Elapsed time in generation " + populationData.getCurrentGeneration() + ": " + String.format("%.4f", elapsedTimeMS) + "ms, with " + String.format("%.4f", observationTimeMS) + "ms observation time");

            final double[] mutationStrengthData = populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.mutationStrength).toArray();
            final double[] mutationProbabilityData = populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.mutationProbability).toArray();
            final double[] crossoverStrengthData = populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.crossoverStrength).toArray();
            final double[] crossoverProbabilityData = populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.crossoverProbability).toArray();

            final StatisticalSummary currentMutationStrength = new DescriptiveStatistics(mutationStrengthData);
            final StatisticalSummary currentMutationProbability = new DescriptiveStatistics(mutationProbabilityData);
            final StatisticalSummary currentCrossoverStrength = new DescriptiveStatistics(crossoverStrengthData);
            final StatisticalSummary currentCrossoverProbability = new DescriptiveStatistics(crossoverProbabilityData);

            final MathArrays.Function median = new Median();

            averageMutationStrength.add(populationData.getCurrentGeneration(), currentMutationStrength.getMean(), currentMutationStrength.getMin(), currentMutationStrength.getMax());
            double medianMutationStrengthEvaluation = median.evaluate(mutationStrengthData);
            medianMutationStrength.add(populationData.getCurrentGeneration(), medianMutationStrengthEvaluation, medianMutationStrengthEvaluation, medianMutationStrengthEvaluation);

            averageMutationProbability.add(populationData.getCurrentGeneration(), currentMutationProbability.getMean(), currentMutationProbability.getMin(), currentMutationProbability.getMax());
            double medianMutationProbabilityEvaluation = median.evaluate(mutationProbabilityData);
            medianMutationProbability.add(populationData.getCurrentGeneration(), medianMutationProbabilityEvaluation, medianMutationProbabilityEvaluation, medianMutationProbabilityEvaluation);

            averageCrossoverStrength.add(populationData.getCurrentGeneration(), currentCrossoverStrength.getMean(), currentCrossoverStrength.getMin(), currentCrossoverStrength.getMax());
            double medianCrossoverStrengthEvaluation = median.evaluate(crossoverStrengthData);
            medianCrossoverStrength.add(populationData.getCurrentGeneration(), medianCrossoverStrengthEvaluation, medianCrossoverStrengthEvaluation, medianCrossoverStrengthEvaluation);

            averageCrossoverProbability.add(populationData.getCurrentGeneration(), currentCrossoverProbability.getMean(), currentCrossoverProbability.getMin(), currentCrossoverProbability.getMax());
            double medianCrossoverProbabilityEvaluation = median.evaluate(crossoverProbabilityData);
            medianCrossoverProbability.add(populationData.getCurrentGeneration(), medianCrossoverProbabilityEvaluation, medianCrossoverProbabilityEvaluation, medianCrossoverProbabilityEvaluation);
        });


        //noinspection MagicNumber
        for (int i = 0; i < 1000000; i++) {
            long startTime = System.nanoTime();
            EventQueue.invokeAndWait(nsga_ii::runGeneration);
            long elapsedTime = System.nanoTime() - startTime;
            System.out.println("; Total elapsed time: " + String.format("%.4f", elapsedTime / 1000000d) + "ms");
            //noinspection MagicNumber
            //Thread.sleep(200);
        }
    }

    static class Function1 extends DefaultOptimizationFunction<double[]> {
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

    static class Function2 extends DefaultOptimizationFunction<double[]> {
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

    static class Function3 extends DefaultOptimizationFunction<double[]> {
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

    static class Function4 extends DefaultOptimizationFunction<double[]> {
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
