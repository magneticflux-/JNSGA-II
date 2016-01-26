package org.skaggs.ec.examples.deb;

import org.apache.commons.math3.util.FastMath;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.examples.numarical.DoubleArrayPopulationGenerator;
import org.skaggs.ec.examples.numarical.SimpleDoubleArrayMutationOperator;
import org.skaggs.ec.multiobjective.NSGA_II;
import org.skaggs.ec.multiobjective.population.Front;
import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.PopulationGenerator;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by skaggsm on 12/27/15.
 */
public final class FON {
    private FON() {
    }

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        XYSeriesCollection collection = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createScatterPlot("FON", "Function 1", "Function 2", collection, PlotOrientation.VERTICAL, true, true, false);
        chart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
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
                .setInt(Key.IntKey.POPULATION_SIZE, 200)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, -10)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, 10)
                .setInt(Key.IntKey.DOUBLE_ARRAY_GENERATION_LENGTH, 3)
                .setDouble(Key.DoubleKey.INITIAL_MUTATION_PROBABILITY, 1d / 3)
                .setDouble(Key.DoubleKey.INITIAL_MUTATION_STRENGTH, .125);

        Operator<double[]> operator = new SimpleDoubleArrayMutationOperator();
        OptimizationFunction<double[]> function1 = new Function1();
        OptimizationFunction<double[]> function2 = new Function2();
        @SuppressWarnings("unchecked")
        OptimizationFunction<double[]>[] optimizationFunctions = new OptimizationFunction[]{function1, function2};
        PopulationGenerator<double[]> populationGenerator = new DoubleArrayPopulationGenerator();

        NSGA_II<double[]> nsga_ii = new NSGA_II<>(properties, operator, optimizationFunctions, populationGenerator);

        nsga_ii.addObserver(populationData -> {
                collection.removeAllSeries();
                for (Front<double[]> front : populationData.getTruncatedPopulation().getFronts()) {
                    XYSeries frontSeries = new XYSeries(front.toString());
                    for (FrontedIndividual<double[]> individual : front.getMembers()) {
                        frontSeries.add(individual.getScore(0), individual.getScore(1));
                    }
                    collection.addSeries(frontSeries);
                }
            });

        //noinspection MagicNumber
        for (int i = 0; i < 10000; i++) {
            SwingUtilities.invokeAndWait(nsga_ii::runGeneration);
            //noinspection MagicNumber
            Thread.sleep(250);
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
        public Key[] requestProperties() {
            return new Key[0];
        }

        @Override
        public double min(Properties properties) {
            return 0;
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
