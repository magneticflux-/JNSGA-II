package org.skaggs;

import org.apache.commons.math3.util.FastMath;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        XYSeriesCollection collection = new XYSeriesCollection();
        JFreeChart chart = ChartFactory.createScatterPlot("Title", "X Axis", "Y Axis", collection, PlotOrientation.VERTICAL, false, true, false);
        //chart.getXYPlot().getDomainAxis().setRange(0, 10);
        //chart.getXYPlot().getRangeAxis().setRange(0, 100);
        ChartPanel panel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        Properties properties = new Properties()
                .setInt(Key.IntKey.POPULATION, 1000)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MINIMUM, -1000)
                .setDouble(Key.DoubleKey.RANDOM_DOUBLE_GENERATION_MAXIMUM, 1000)
                .setDouble(Key.DoubleKey.INITIAL_MUTATION_PROBABILITY, 1);

        Operator<Double> operator = new Operator<Double>() {
            @Override
            public Population<Double> apply(FrontedPopulation<Double> population, Properties properties) {
                Random r = new Random();
                double probability = properties.getDouble(Key.DoubleKey.INITIAL_MUTATION_PROBABILITY);
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
                return new Key[]{Key.DoubleKey.INITIAL_MUTATION_PROBABILITY};
            }
        };
        OptimizationFunction<Double> function1 = new OptimizationFunction<Double>() {
            @Override
            public double evaluate(Double object, Properties properties) {
                return FastMath.pow(object - 1, 2);
            }

            @Override
            public int compare(Double o1, Double o2) {
                return -Double.compare(o1, o2); // Smaller is better
            }

            @Override
            public double min(Properties properties) {
                return 0;
            }

            @Override
            public double max(Properties properties) {
                return 1000 * 1000;
            }


            @Override
            public Key[] requestProperties() {
                return new Key[0];
            }
        };
        OptimizationFunction<Double> function2 = new OptimizationFunction<Double>() {
            @Override
            public double evaluate(Double object, Properties properties) {
                return FastMath.pow(object - 3, 2);
            }

            @Override
            public double min(Properties properties) {
                return 0;
            }

            @Override
            public double max(Properties properties) {
                return 1000 * 1000;
            }

            @Override
            public int compare(Double o1, Double o2) {
                return -Double.compare(o1, o2);
            }

            @Override
            public Key[] requestProperties() {
                return new Key[0];
            }
        };
        List<OptimizationFunction<Double>> optimizationFunctions = Arrays.asList(function1, function2);
        PopulationGenerator<Double> populationGenerator = new DoublePopulationGenerator();

        NSGA_II<Double> nsgaii = new NSGA_II<>(properties, operator, optimizationFunctions, populationGenerator);

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
            System.out.println("Total crowding distance: " + populationData.getFrontedPopulation().getPopulation().parallelStream().mapToDouble(FrontedIndividual::getCrowdingScore).filter(Double::isFinite).sum());
        });

        for (int i = 0; i < 100; i++) {
            nsgaii.runGeneration();
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
