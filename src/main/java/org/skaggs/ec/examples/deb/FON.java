package org.skaggs.ec.examples.deb;

import org.apache.commons.math3.util.FastMath;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.examples.DoubleArrayPopulationGenerator;
import org.skaggs.ec.examples.SimpleDoubleArrayMutationOperator;
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
public class FON {
    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        XYSeriesCollection collection = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createScatterPlot("Title", "X Axis", "Y Axis", collection, PlotOrientation.VERTICAL, true, true, false);
        chart.getXYPlot().getDomainAxis().setRange(0, 1);
        chart.getXYPlot().getRangeAxis().setRange(0, 1);
        ChartPanel panel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.add(panel);
        //noinspection MagicNumber
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        @SuppressWarnings("MagicNumber")
        Properties properties = new Properties()
                .setInt(Key.IntKey.INT_POPULATION, 500)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, -4)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, 4)
                .setInt(Key.IntKey.DOUBLE_ARRAY_GENERATION_LENGTH, 3)
                .setDouble(Key.DoubleKey.MUTATION_PROBABILITY, 1)
                .setDouble(Key.DoubleKey.DOUBLE_MUTATION_RANGE, .125);

        Operator<double[]> operator = new SimpleDoubleArrayMutationOperator();
        OptimizationFunction<double[]> function1 = new Function1();
        OptimizationFunction<double[]> function2 = new Function2();
        List<OptimizationFunction<double[]>> optimizationFunctions = Arrays.asList(function1, function2);
        PopulationGenerator<double[]> populationGenerator = new DoubleArrayPopulationGenerator();

        NSGA_II<double[]> nsga_ii = new NSGA_II<>(properties, operator, optimizationFunctions, populationGenerator);

        collection.addSeries(new XYSeries("Accepted"));
        collection.addSeries(new XYSeries("Rejected"));

        nsga_ii.addObserver(populationData -> {
            XYSeries accepted = collection.getSeries("Accepted");
            XYSeries rejected = collection.getSeries("Rejected");
            accepted.clear();
            rejected.clear();

            for (FrontedIndividual<double[]> individual : populationData.getTruncatedPopulation().getPopulation())
                accepted.add(individual.getScore(function1), individual.getScore(function2));
            populationData.getFrontedPopulation().getPopulation().stream().filter(doubleFrontedIndividual -> !populationData.getTruncatedPopulation().getPopulation().contains(doubleFrontedIndividual)).forEachOrdered(individual -> rejected.add(individual.getScore(function1), individual.getScore(function2)));
            //System.out.println("Total crowding distance: " + populationData.getFrontedPopulation().getPopulation().parallelStream().mapToDouble(FrontedIndividual::getCrowdingScore).filter(Double::isFinite).sum());
            //System.out.println(populationData.getFrontedPopulation().getPopulation().stream().map(individual -> Arrays.toString(individual.getIndividual())).collect(Collectors.joining(", ")));
        });

        for (int i = 0; i < 1000; i++) {
            SwingUtilities.invokeAndWait(nsga_ii::runGeneration);
            Thread.sleep(50);
        }
    }

    static class Function1 implements OptimizationFunction<double[]> {
        @Override
        public double evaluate(double[] object, Properties properties) {
            assert object.length == 3;
            return 1 - FastMath.exp(-(this.f(object[0]) + this.f(object[1]) + this.f(object[2])));
        }

        private double f(double x) {
            return FastMath.pow(x - (1 / FastMath.sqrt(3)), 2);
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

        @Override
        public double max(Properties properties) {
            return 1;
        }


    }

    static class Function2 implements OptimizationFunction<double[]> {
        @Override
        public double evaluate(double[] object, Properties properties) {
            assert object.length == 3;
            return 1 - FastMath.exp(-(this.f(object[0]) + this.f(object[1]) + this.f(object[2])));
        }

        private double f(double x) {
            return FastMath.pow(x + (1 / FastMath.sqrt(3)), 2);
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
