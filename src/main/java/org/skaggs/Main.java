package org.skaggs;

import org.apache.commons.math3.util.FastMath;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.skaggs.ec.OptimizationFunction;
import org.skaggs.ec.examples.DoublePopulationGenerator;
import org.skaggs.ec.multiobjective.NSGA_II;
import org.skaggs.ec.multiobjective.population.FrontedIndividual;
import org.skaggs.ec.multiobjective.population.FrontedPopulation;
import org.skaggs.ec.operators.Operator;
import org.skaggs.ec.population.Individual;
import org.skaggs.ec.population.Population;
import org.skaggs.ec.population.PopulationGenerator;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import javax.swing.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        List<Double> list = Arrays.asList(1d, 3d, 2d, 5d, -3d); // Default number sorts go LOWEST to HIGHEST
        Collections.sort(list);
        System.out.println(list);

        DefaultXYDataset dataset = new DefaultXYDataset();
        double[][] array = new double[][]{
                {-2, -1, 0, 1, 2, 3, 4}, // X
                {4, 1, 0, 1, 4, 9, 16} // Y
        };
        dataset.addSeries("Series1", array);
        XYSeriesCollection collection = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createScatterPlot("Title", "X Axis", "Y Axis", collection, PlotOrientation.VERTICAL, false, true, false);
        chart.getXYPlot().getDomainAxis().setRange(0, 10);
        chart.getXYPlot().getRangeAxis().setRange(0, 100);
        ChartPanel panel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        dataset.addSeries("Series1", array);


        Properties properties = new Properties()
                .setInt(Key.IntKey.INT_POPULATION, 100)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, -100)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, 100)
                .setDouble(Key.DoubleKey.MUTATION_PROBABILITY, 1);

        Operator<Double> operator = new Operator<Double>() {
            @Override
            public Population<Double> apply(FrontedPopulation<Double> population, Properties properties) {
                Random r = new Random();
                double probability = properties.getDouble(Key.DoubleKey.MUTATION_PROBABILITY);
                List<Individual<Double>> individuals = new ArrayList<>(population.getPopulation().size());

                for (FrontedIndividual<Double> d : population.getPopulation()) {
                    if (r.nextDouble() < probability)
                        individuals.add(new Individual<>(mutate(d.getIndividual(), r)));
                    else
                        individuals.add(new Individual<>(d.getIndividual()));
                }

                return new Population<>(individuals);
            }

            private double mutate(double d, Random r) {
                double var = d + r.nextDouble() - .5;
                if (var > 1000) return 1000;
                else if (var < -1000) return -1000;
                else return var;
            }

            @Override
            public Key[] requestProperties() {
                return new Key[]{Key.DoubleKey.MUTATION_PROBABILITY};
            }
        };
        OptimizationFunction<Double> function1 = new OptimizationFunction<Double>() {
            @Override
            public double evaluate(Double object) {
                return FastMath.pow(object - 1, 2);
            }

            @Override
            public double min() {
                return 0;
            }

            @Override
            public double max() {
                return 1000 * 1000;
            }

            @Override
            public int compare(Double o1, Double o2) {
                return -Double.compare(o1, o2); // Smaller is better
            }
        };
        OptimizationFunction<Double> function2 = new OptimizationFunction<Double>() {
            @Override
            public double evaluate(Double object) {
                return FastMath.pow(object - 3, 2);
            }

            @Override
            public double min() {
                return 0;
            }

            @Override
            public double max() {
                return 1000 * 1000;
            }

            @Override
            public int compare(Double o1, Double o2) {
                return -Double.compare(o1, o2);
            }
        };
        List<OptimizationFunction<Double>> optimizationFunctions = Arrays.asList(function1, function2);
        PopulationGenerator<Double> populationGenerator = new DoublePopulationGenerator();

        NSGA_II<Double> nsgaii = new NSGA_II<>(properties, operator, optimizationFunctions, populationGenerator);

        /*
        nsgaii.addObserver(populationData -> {
            //for (int i = 0; i < dataset.getSeriesCount(); i++) {
            //    dataset.removeSeries(dataset.getSeriesKey(i));
            //}

            collection.removeAllSeries();
            for (Front<Double> front : populationData.getFrontedPopulation().getFronts()) {
                XYSeries series = new XYSeries(front.toString());
                //double[][] data = new double[2][];
                //data[0] = front.getMembers().stream().mapToDouble(FrontedIndividual::getIndividual).toArray();
                //data[1] = front.getMembers().stream().mapToDouble(value -> value.getScore(function1) + value.getScore(function2)).toArray();
                for (FrontedIndividual<Double> individual : front.getMembers()) {
                    series.add(individual.getIndividual(), new Double(individual.getScore(function1) + individual.getScore(function2))); // Dumb overloading of Number and double
                }
                //dataset.addSeries(front.toString(), data);
                collection.addSeries(series);
            }
            //System.out.println(populationData.getFrontedPopulation().getFrontedPopulation());
            //populationData.getFrontedPopulation().getFrontedPopulation().forEach(System.out::println);
            populationData.getFrontedPopulation().sort();
            System.out.println(populationData.getFrontedPopulation());
            System.out.println(populationData.getFrontedPopulation().getPopulation().parallelStream().mapToDouble(FrontedIndividual::getCrowdingScore).filter(Double::isFinite).sum());
        });
        */

        nsgaii.addObserver(populationData -> {
            collection.removeAllSeries();
            XYSeries accepted = new XYSeries("Accepted");
            XYSeries rejected = new XYSeries("Rejected");
            for (FrontedIndividual<Double> individual : populationData.getTruncatedPopulation().getPopulation())
                accepted.add(individual.getIndividual(), new Double(individual.getScore(function1) + individual.getScore(function2)));
            populationData.getFrontedPopulation().getPopulation().stream().filter(new Predicate<FrontedIndividual<Double>>() {
                @Override
                public boolean test(FrontedIndividual<Double> doubleFrontedIndividual) {
                    for (FrontedIndividual<Double> individual : populationData.getTruncatedPopulation().getPopulation()) {
                        if (doubleFrontedIndividual.getIndividual().equals(individual.getIndividual()))
                            return false;
                    }
                    return true;
                }
            }).forEachOrdered(new Consumer<FrontedIndividual<Double>>() {
                @Override
                public void accept(FrontedIndividual<Double> individual) {
                    rejected.add(individual.getIndividual(), new Double(individual.getScore(function1) + individual.getScore(function2)));
                }
            });
            collection.addSeries(accepted);
            collection.addSeries(rejected);
        });

        new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                nsgaii.runGeneration();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
