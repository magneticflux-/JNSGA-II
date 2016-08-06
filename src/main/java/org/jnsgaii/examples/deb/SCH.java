package org.jnsgaii.examples.deb;

import org.apache.commons.math3.util.FastMath;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jnsgaii.examples.numarical.DoublePopulationGenerator;
import org.jnsgaii.examples.numarical.SimpleDoubleMutationOperator;
import org.jnsgaii.functions.DefaultOptimizationFunction;
import org.jnsgaii.functions.OptimizationFunction;
import org.jnsgaii.multiobjective.NSGAII;
import org.jnsgaii.multiobjective.population.Front;
import org.jnsgaii.multiobjective.population.FrontedIndividual;
import org.jnsgaii.operators.Operator;
import org.jnsgaii.population.PopulationGenerator;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by skaggsm on 12/27/15.
 */
public final class SCH {
    private SCH() {
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        XYSeriesCollection collection = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createScatterPlot("SCH", "Function 1", "Function 2", collection, PlotOrientation.VERTICAL, true, true, false);
        chart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
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
                .setInt(Key.IntKey.DefaultIntKey.POPULATION_SIZE, 500)
                .setDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, -100)
                .setDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, 100)
                .setDouble(Key.DoubleKey.DefaultDoubleKey.INITIAL_MUTATION_PROBABILITY, 1)
                .setDouble(Key.DoubleKey.DefaultDoubleKey.INITIAL_MUTATION_STRENGTH, .25);

        Operator<Double> operator = new SimpleDoubleMutationOperator();
        OptimizationFunction<Double> function1 = new Function1();
        OptimizationFunction<Double> function2 = new Function2();
        @SuppressWarnings("unchecked")
        List<OptimizationFunction<Double>> optimizationFunctions = Arrays.asList(function1, function2);
        PopulationGenerator<Double> populationGenerator = new DoublePopulationGenerator();

        NSGAII<Double> nsgaii = new NSGAII<>(properties, operator, optimizationFunctions, populationGenerator);

        nsgaii.addObserver(populationData -> {
            collection.removeAllSeries();
            for (Front<Double> front : populationData.getTruncatedPopulation().getFronts()) {
                XYSeries frontSeries = new XYSeries(front.toString());
                for (FrontedIndividual<Double> individual : front.getMembers()) {
                    frontSeries.add(individual.getScore(0), individual.getScore(1));
                }
                collection.addSeries(frontSeries);
            }
        });

        //noinspection MagicNumber
        for (int i = 0; i < 10000; i++) {
            SwingUtilities.invokeAndWait(nsgaii::runGeneration);
            //noinspection MagicNumber
            Thread.sleep(250);
        }
    }

    static class Function1 extends DefaultOptimizationFunction<Double> {
        @Override
        public int compare(Double o1, Double o2) {
            return -Double.compare(o1, o2);
        }

        @Override
        public Key[] requestProperties() {
            return new Key[]{Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM};
        }

        @Override
        public double evaluateIndividual(Double object, HashMap<String, Object> computationResults, Properties properties) {
            return FastMath.pow(object, 2);
        }


        @Override
        public double min(Properties properties) {
            return FastMath.min(0, FastMath.min(this.evaluateIndividual(properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM), null, properties), this.evaluateIndividual(properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM), null, properties)));
        }

        @Override
        public double max(Properties properties) {
            return FastMath.max(0, FastMath.max(this.evaluateIndividual(properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM), null, properties), this.evaluateIndividual(properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM), null, properties)));
        }

        @Override
        public boolean isDeterministic() {
            return true;
        }


    }

    static class Function2 extends DefaultOptimizationFunction<Double> {
        @Override
        public double evaluateIndividual(Double object, HashMap<String, Object> computationResults, Properties properties) {
            return FastMath.pow(object - 2, 2);
        }

        @Override
        public double min(Properties properties) {
            return FastMath.min(0, FastMath.min(this.evaluateIndividual(properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM), null, properties), this.evaluateIndividual(properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM), null, properties)));
        }

        @Override
        public double max(Properties properties) {
            return FastMath.max(0, FastMath.max(this.evaluateIndividual(properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM), null, properties), this.evaluateIndividual(properties.getDouble(Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM), null, properties)));
        }

        @Override
        public boolean isDeterministic() {
            return true;
        }

        @Override
        public int compare(Double o1, Double o2) {
            return -Double.compare(o1, o2);
        }

        @Override
        public Key[] requestProperties() {
            return new Key[]{Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, Key.DoubleKey.DefaultDoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM};
        }
    }
}
