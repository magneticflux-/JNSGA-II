package org.skaggs.ec.examples.deb;

import org.apache.commons.math3.util.FastMath;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.examples.DoublePopulationGenerator;
import org.skaggs.ec.examples.SimpleDoubleMutationOperator;
import org.skaggs.ec.multiobjective.NSGA_II;
import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.PopulationGenerator;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * Created by skaggsm on 12/27/15.
 */
public class SCH {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        XYSeriesCollection collection = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createScatterPlot("SCH", "Function 1", "Function 2", collection, PlotOrientation.VERTICAL, true, true, false);
        chart.getXYPlot().getDomainAxis().setRange(0, 10);
        chart.getXYPlot().getRangeAxis().setRange(0, 10);
        ChartPanel panel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.add(panel);
        //noinspection MagicNumber
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        @SuppressWarnings("MagicNumber")
        Properties properties = new Properties()
                .setInt(Key.IntKey.INT_POPULATION, 100)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, -100)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, 100)
                .setDouble(Key.DoubleKey.MUTATION_PROBABILITY, 1)
                .setDouble(Key.DoubleKey.DOUBLE_MUTATION_RANGE, .125);

        Operator<Double> operator = new SimpleDoubleMutationOperator();
        OptimizationFunction<Double> function1 = new Function1();
        OptimizationFunction<Double> function2 = new Function2();
        List<OptimizationFunction<Double>> optimizationFunctions = Arrays.asList(function1, function2);
        PopulationGenerator<Double> populationGenerator = new DoublePopulationGenerator();

        NSGA_II<Double> nsga_ii = new NSGA_II<>(properties, operator, optimizationFunctions, populationGenerator);

        collection.addSeries(new XYSeries("Accepted"));
        collection.addSeries(new XYSeries("Rejected"));

        nsga_ii.addObserver(populationData -> {
            XYSeries accepted = collection.getSeries("Accepted");
            XYSeries rejected = collection.getSeries("Rejected");
            accepted.clear();
            rejected.clear();

            for (FrontedIndividual<Double> individual : populationData.getTruncatedPopulation().getPopulation())
                accepted.add(individual.getScore(function1), individual.getScore(function2));
            populationData.getFrontedPopulation().getPopulation().stream().filter(doubleFrontedIndividual -> !populationData.getTruncatedPopulation().getPopulation().contains(doubleFrontedIndividual)).forEachOrdered(individual -> rejected.add(individual.getScore(function1), individual.getScore(function2)));
            //System.out.println("Total crowding distance: " + populationData.getFrontedPopulation().getPopulation().parallelStream().mapToDouble(FrontedIndividual::getCrowdingScore).filter(Double::isFinite).sum());
        });

        for (int i = 0; i < 10000; i++) {
            SwingUtilities.invokeAndWait(nsga_ii::runGeneration);
            Thread.sleep(16);
        }
    }

    static class Function1 implements OptimizationFunction<Double> {
        @Override
        public int compare(Double o1, Double o2) {
            return -Double.compare(o1, o2);
        }

        @Override
        public Key[] requestProperties() {
            return new Key[]{Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM};
        }

        @Override
        public double evaluate(Double object, Properties properties) {
            return FastMath.pow(object, 2);
        }


        @Override
        public double min(Properties properties) {
            return FastMath.min(0, FastMath.min(this.evaluate(properties.getDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM), properties), this.evaluate(properties.getDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM), properties)));
        }

        @Override
        public double max(Properties properties) {
            return FastMath.max(0, FastMath.max(this.evaluate(properties.getDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM), properties), this.evaluate(properties.getDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM), properties)));
        }


    }

    static class Function2 implements OptimizationFunction<Double> {
        @Override
        public double evaluate(Double object, Properties properties) {
            return FastMath.pow(object - 2, 2);
        }

        @Override
        public double min(Properties properties) {
            return FastMath.min(0, FastMath.min(this.evaluate(properties.getDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM), properties), this.evaluate(properties.getDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM), properties)));
        }

        @Override
        public double max(Properties properties) {
            return FastMath.max(0, FastMath.max(this.evaluate(properties.getDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM), properties), this.evaluate(properties.getDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM), properties)));
        }

        @Override
        public int compare(Double o1, Double o2) {
            return -Double.compare(o1, o2);
        }

        @Override
        public Key[] requestProperties() {
            return new Key[]{Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM};
        }
    }
}
