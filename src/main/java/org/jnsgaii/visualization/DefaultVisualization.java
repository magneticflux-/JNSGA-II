package org.jnsgaii.visualization;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jnsgaii.OptimizationFunction;
import org.jnsgaii.UpdatableHistogramDataset;
import org.jnsgaii.multiobjective.NSGA_II;
import org.jnsgaii.operators.DefaultOperator;
import org.jnsgaii.population.PopulationData;
import org.jnsgaii.properties.Key;
import org.jnsgaii.properties.Properties;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

/**
 * Created by Mitchell on 3/30/2016.
 */
public final class DefaultVisualization {
    private DefaultVisualization() {
    }

    public static <E> void displayGenerationGraph(String[] aspectDescriptions, String[] scoreDescriptions, Iterable<PopulationData<E>> generationData) {
        JFrame window = new JFrame("Generations");
        GroupLayout layout = new GroupLayout(window);
        window.setLayout(layout);

        YIntervalSeriesCollection averageAspectCollection = new YIntervalSeriesCollection();
        JFreeChart averageAspectChart = ChartFactory.createScatterPlot("Average Aspect Values", "Generation", "Aspect Values", averageAspectCollection, PlotOrientation.VERTICAL, true, false, false);
        averageAspectChart.getXYPlot().setRenderer(new XYErrorRenderer());
        ChartPanel averageAspectPanel = new ChartPanel(averageAspectChart);

        YIntervalSeriesCollection averageScoreCollection = new YIntervalSeriesCollection();
        JFreeChart averageScoreChart = ChartFactory.createScatterPlot("Average Scores", "Generation", "Scores", averageScoreCollection, PlotOrientation.VERTICAL, true, false, false);
        averageAspectChart.getXYPlot().setRenderer(new XYErrorRenderer());
        ChartPanel averageScorePanel = new ChartPanel(averageScoreChart);

        layout.setVerticalGroup(
                layout.createParallelGroup()
                        .addComponent(averageScorePanel)
                        .addComponent(averageAspectPanel)
        );
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addComponent(averageScorePanel)
                        .addComponent(averageAspectPanel)
        );

        for (PopulationData<E> populationData : generationData) {
            for (int i = 0; i < populationData.getTruncatedPopulation().getPopulation().get(0).aspects.length; i++) {
                YIntervalSeries aspectSeries;
                DescriptiveStatistics aspectSummary;

                try {
                    aspectSeries = averageAspectCollection.getSeries(i);
                } catch (IllegalArgumentException e) {
                    aspectSeries = new YIntervalSeries(aspectDescriptions[i]);
                    averageAspectCollection.addSeries(aspectSeries);
                }

                final int finalI = i;
                aspectSummary = new DescriptiveStatistics(populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.aspects[finalI]).toArray());
                //noinspection MagicNumber
                aspectSeries.add(populationData.getCurrentGeneration(), aspectSummary.getPercentile(50), aspectSummary.getPercentile(25), aspectSummary.getPercentile(75));
            }

            for (int i = 0; i < populationData.getTruncatedPopulation().getPopulation().get(0).getScores().length; i++) {
                YIntervalSeries scoreSeries;
                DescriptiveStatistics scoreSummary;

                try {
                    scoreSeries = averageScoreCollection.getSeries(i);
                } catch (IllegalArgumentException e) {
                    scoreSeries = new YIntervalSeries(scoreDescriptions[i]);
                    averageScoreCollection.addSeries(scoreSeries);
                }

                final int finalI = i;
                scoreSummary = new DescriptiveStatistics(populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.getScore(finalI)).toArray());
                scoreSeries.add(populationData.getCurrentGeneration(), scoreSummary.getPercentile(50), scoreSummary.getPercentile(25), scoreSummary.getPercentile(75));
            }
        }

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
    }

    public static <E> void startInterface(@SuppressWarnings("TypeMayBeWeakened") DefaultOperator<E> operator, List<OptimizationFunction<E>> optimizationFunctions, NSGA_II<E> nsga_ii, Properties properties) {
        //Thread.sleep(5000);

        Box histogramPanel = new Box(BoxLayout.Y_AXIS);
        JScrollPane histogramScrollPane = new JScrollPane(histogramPanel);
        histogramScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        /*
        XYSeriesCollection currentGenerationCollection = new XYSeriesCollection();
        JFreeChart currentGenerationChart = ChartFactory.createScatterPlot("Functions", "Function 1", "Function 2", currentGenerationCollection, PlotOrientation.VERTICAL, true, false, false);
        currentGenerationChart.getXYPlot().setRenderer(new XYLineAndShapeRenderer(true, true));
        ChartPanel currentGenerationPanel = new ChartPanel(currentGenerationChart);

        XYSeriesCollection currentPopulationCollection = new XYSeriesCollection();
        JFreeChart currentPopulationChart = ChartFactory.createScatterPlot("Individuals", "", "", currentPopulationCollection, PlotOrientation.VERTICAL, true, false, false);
        currentPopulationChart.getXYPlot().getDomainAxis().setAttributedLabel(new AttributedString("X\u2081"));
        currentPopulationChart.getXYPlot().getRangeAxis().setAttributedLabel(new AttributedString("X\u2082"));
        ChartPanel currentPopulationPanel = new ChartPanel(currentPopulationChart);
        */

        YIntervalSeriesCollection averageAspectCollection = new YIntervalSeriesCollection();
        JFreeChart averageAspectChart = ChartFactory.createScatterPlot("Average Aspect Values", "Generation", "Aspect Values", averageAspectCollection, PlotOrientation.VERTICAL, true, false, false);
        averageAspectChart.getXYPlot().setRenderer(new XYErrorRenderer());
        ChartPanel averageAspectPanel = new ChartPanel(averageAspectChart);

        JFrame windowFrame = new JFrame("Evolutionary Algorithm");
        JPanel mainPanel = new JPanel();
        windowFrame.setLayout(new BorderLayout());
        windowFrame.add(mainPanel, BorderLayout.CENTER);

        GroupLayout groupLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setAutoCreateContainerGaps(true);

        groupLayout.setHorizontalGroup(
                groupLayout.createSequentialGroup()
                        .addComponent(histogramScrollPane)
                        //.addGroup(groupLayout.createParallelGroup()
                        //        .addComponent(currentGenerationPanel)
                        //        .addComponent(currentPopulationPanel))
                        .addComponent(averageAspectPanel)
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup()
                        .addComponent(histogramScrollPane)
                        //.addGroup(groupLayout.createSequentialGroup()
                        //        .addComponent(currentGenerationPanel)
                        //        .addComponent(currentPopulationPanel))
                        .addComponent(averageAspectPanel)
        );

        String[] aspectDescriptions = operator.getAspectDescriptions();

        UpdatableHistogramDataset[] datasets = new UpdatableHistogramDataset[optimizationFunctions.size()];
        for (int i = 0; i < datasets.length; i++) {
            datasets[i] = new UpdatableHistogramDataset();
            ChartPanel panel = new ChartPanel(ChartFactory.createHistogram(optimizationFunctions.get(i).getClass().getSimpleName(), "Score", "Frequency", datasets[i], PlotOrientation.VERTICAL, true, false, false));
            histogramPanel.add(panel);
        }


        //noinspection MagicNumber
        windowFrame.setSize(1400, 1000);
        //noinspection MagicNumber
        windowFrame.setLocation(0, 0);
        windowFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        windowFrame.setVisible(true);

        nsga_ii.addObserver(populationData -> {
            try {
                EventQueue.invokeAndWait(() -> {
                    for (int i = 0; i < datasets.length; i++) {
                        datasets[i].removeAllSeries();
                        final int finalI = i;
                        datasets[i].addSeries("Individuals", populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.getScore(finalI)).toArray(), 20);
                    }
                });
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
/*
        nsga_ii.addObserver(populationData -> {
            currentGenerationChart.setNotify(false);
            currentGenerationCollection.removeAllSeries();
            for (Front<E> front : populationData.getTruncatedPopulation().getFronts()) {
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
            for (Front<E> front : populationData.getTruncatedPopulation().getFronts()) {
                XYSeries frontSeries = new XYSeries(front.toString());
                for (FrontedIndividual<double[]> individual : front.getMembers()) {
                    frontSeries.add(individual.getIndividual()[0], individual.getIndividual()[1]);
                }
                currentPopulationCollection.addSeries(frontSeries);
            }
            currentPopulationChart.setNotify(true);
        });
*/
        nsga_ii.addObserver(populationData -> {
            try {
                EventQueue.invokeAndWait(() -> {
                    double elapsedTimeMS = (populationData.getElapsedTime() / 1000000d);
                    double observationTimeMS = (populationData.getPreviousObservationTime() / 1000000d);
                    System.out.println("Elapsed time in generation " + populationData.getCurrentGeneration() + ": " + String.format("%.4f", elapsedTimeMS) + "ms, with " + String.format("%.4f", observationTimeMS) + "ms observation time");

                    for (int i = 0; i < ((double[]) properties.getValue(Key.DoubleKey.DefaultDoubleKey.INITIAL_ASPECT_ARRAY)).length; i++) {
                        YIntervalSeries aspectSeries;
                        DescriptiveStatistics aspectSummary;

                        try {
                            aspectSeries = averageAspectCollection.getSeries(i);
                        } catch (IllegalArgumentException e) {
                            aspectSeries = new YIntervalSeries(aspectDescriptions[i]);
                            averageAspectCollection.addSeries(aspectSeries);
                        }

                        final int finalI = i;
                        aspectSummary = new DescriptiveStatistics(populationData.getTruncatedPopulation().getPopulation().parallelStream().mapToDouble(value -> value.aspects[finalI]).toArray());
                        //noinspection MagicNumber
                        aspectSeries.add(populationData.getCurrentGeneration(), aspectSummary.getPercentile(50), aspectSummary.getPercentile(25), aspectSummary.getPercentile(75));
                    }
                });
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }
}
