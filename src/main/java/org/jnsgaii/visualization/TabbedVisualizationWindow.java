package org.jnsgaii.visualization;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.XYErrorRenderer;
import org.jfree.data.xy.CategoryTableXYDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jnsgaii.UpdatableHistogramDataset;
import org.jnsgaii.computations.Computation;
import org.jnsgaii.functions.OptimizationFunction;
import org.jnsgaii.observation.EvolutionObservable;
import org.jnsgaii.operators.DefaultOperator;
import org.jnsgaii.population.PopulationData;
import org.jppf.client.JPPFClient;
import org.jppf.client.event.ClientQueueEvent;
import org.jppf.client.event.ClientQueueListener;
import org.jppf.client.event.JobEvent;
import org.jppf.client.event.JobListener;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.Component;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by Mitchell on 10/7/2016.
 */
public class TabbedVisualizationWindow extends JFrame {
    private final JTabbedPane jTabbedPane;

    public TabbedVisualizationWindow() {
        super("Evolutionary Algorithm Visualization");
        jTabbedPane = new JTabbedPane();
        add(jTabbedPane);
    }

    public void addTab(String title, Component component) {
        try {
            EventQueue.invokeAndWait(() -> jTabbedPane.addTab(title, component));
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    public <E> TabbedVisualizationWindow addCurrentScoreDistributionsTab(EvolutionObservable<E> evolutionObservable, List<OptimizationFunction<E>> optimizationFunctions) {
        Box histogramPanel = new Box(BoxLayout.Y_AXIS);
        JScrollPane histogramScrollPane = new JScrollPane(histogramPanel);
        histogramScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        UpdatableHistogramDataset[] datasets = new UpdatableHistogramDataset[optimizationFunctions.size()];
        for (int i = 0; i < datasets.length; i++) {
            datasets[i] = new UpdatableHistogramDataset();
            ChartPanel panel = new ChartPanel(ChartFactory.createHistogram(optimizationFunctions.get(i).getClass().getSimpleName(), "Score", "Frequency", datasets[i], PlotOrientation.VERTICAL, true, false, false));
            histogramPanel.add(panel);
        }
        evolutionObservable.addObserver(populationData -> {
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

        addTab("Current Score Distributions", histogramScrollPane);

        return this;
    }

    @SuppressWarnings("Duplicates")
    public <E> TabbedVisualizationWindow addPriorScoresTab(EvolutionObservable<E> evolutionObservable, List<OptimizationFunction<E>> optimizationFunctions) {
        YIntervalSeriesCollection priorScoresCollection = new YIntervalSeriesCollection();
        JFreeChart priorScoresChart = ChartFactory.createScatterPlot("Prior Scores", "Generation", "Scores (max/mean/min)", priorScoresCollection, PlotOrientation.VERTICAL, true, false, false);
        priorScoresChart.getXYPlot().setRenderer(new XYErrorRenderer());
        ChartPanel priorScoresPanel = new ChartPanel(priorScoresChart);

        evolutionObservable.addObserver(populationData -> {
            try {
                EventQueue.invokeAndWait(() -> {
                    for (int i = 0; i < optimizationFunctions.size(); i++) {
                        YIntervalSeries scoreSeries;
                        try {
                            scoreSeries = priorScoresCollection.getSeries(i);
                        } catch (IllegalArgumentException e) {
                            scoreSeries = new YIntervalSeries(optimizationFunctions.get(i).getClass().getSimpleName());
                            priorScoresCollection.addSeries(scoreSeries);
                        }
                        final int finalI = i;
                        DescriptiveStatistics scores = new DescriptiveStatistics(populationData.getTruncatedPopulation().getPopulation().stream().mapToDouble(value -> value.getScore(finalI)).toArray());
                        scoreSeries.add(populationData.getCurrentGeneration(), scores.getMean(), scores.getMin(), scores.getMax());
                    }
                });
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        addTab("Prior Scores", priorScoresPanel);

        return this;
    }

    @SuppressWarnings("Duplicates")
    public <E> TabbedVisualizationWindow addMedianAspectValuesTab(EvolutionObservable<E> evolutionObservable, DefaultOperator<E> defaultOperator) {
        YIntervalSeriesCollection averageAspectCollection = new YIntervalSeriesCollection();
        JFreeChart averageAspectChart = ChartFactory.createScatterPlot("Average Aspect Values", "Generation", "Aspect Values (25/50/75)", averageAspectCollection, PlotOrientation.VERTICAL, true, false, false);
        averageAspectChart.getXYPlot().setRenderer(new XYErrorRenderer());
        ChartPanel averageAspectPanel = new ChartPanel(averageAspectChart);

        String[] aspectDescriptions = defaultOperator.getAspectDescriptions();

        evolutionObservable.addObserver(populationData -> {
            try {
                EventQueue.invokeAndWait(() -> {
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
                });
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        addTab("Median Aspect Values", averageAspectPanel);

        return this;
    }

    @SuppressWarnings("Duplicates")
    public <E> TabbedVisualizationWindow addElapsedTimesTab(EvolutionObservable<E> evolutionObservable, List<OptimizationFunction<E>> optimizationFunctions, List<Computation<E, ?>> computations) {
        CategoryTableXYDataset elapsedTimesDataset = new CategoryTableXYDataset();
        NumberAxis elapsedTimesChartDomainAxis = new NumberAxis("Generation");
        elapsedTimesChartDomainAxis.setAutoRangeIncludesZero(false);
        NumberAxis elapsedTimesChartRangeAxis = new NumberAxis("Elapsed Time (ms)");
        XYPlot elapsedTimesChartPlot = new XYPlot(elapsedTimesDataset, elapsedTimesChartDomainAxis, elapsedTimesChartRangeAxis, new StackedXYBarRenderer(.25));
        elapsedTimesChartPlot.setOrientation(PlotOrientation.VERTICAL);
        JFreeChart elapsedTimesChart = new JFreeChart("Elapsed Times", JFreeChart.DEFAULT_TITLE_FONT, elapsedTimesChartPlot, true);//ChartFactory.createStackedBarChart3D("Elapsed Times", "Generation", "Elapsed Time (ms)", elapsedTimesDataset, PlotOrientation.VERTICAL, true, false, false);
        StandardChartTheme.createJFreeTheme().apply(elapsedTimesChart);
        ChartPanel elapsedTimesPanel = new ChartPanel(elapsedTimesChart);

        evolutionObservable.addObserver(populationData -> {
            try {
                EventQueue.invokeAndWait(() ->
                {
                    System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME) + " Elapsed time in generation " + populationData.getCurrentGeneration() + ": " +
                            DurationFormatUtils.formatDurationWords(TimeUnit.NANOSECONDS.toMillis(populationData.getTotalTime()), true, true) +
                            ", with " +
                            DurationFormatUtils.formatDurationWords(TimeUnit.NANOSECONDS.toMillis(populationData.getPreviousObservationTime()), true, true) +
                            " observation time. Population size: " + populationData.getFrontedPopulation().getPopulation().size());

                    elapsedTimesDataset.add(populationData.getCurrentGeneration(), TimeUnit.NANOSECONDS.toMillis(populationData.getOperatorApplyingTime()), "Operator Applying Time (ms)");
                    elapsedTimesDataset.add(populationData.getCurrentGeneration(), TimeUnit.NANOSECONDS.toMillis(populationData.getMergingTime()), "Merging Time (ms)");
                    long[] computationTimes = populationData.getComputationTimes();
                    for (int i = 0; i < computationTimes.length; i++) {
                        elapsedTimesDataset.add(populationData.getCurrentGeneration(), TimeUnit.NANOSECONDS.toMillis(computationTimes[i]), "Computation Time of \"" + computations.get(i).getComputationID() + "\" (ms)");
                    }
                    long[] optimizationFunctionTimes = populationData.getOptimizationFunctionTimes();
                    for (int i = 0; i < optimizationFunctionTimes.length; i++) {
                        elapsedTimesDataset.add(populationData.getCurrentGeneration(), TimeUnit.NANOSECONDS.toMillis(optimizationFunctionTimes[i]), "Optimization Function Time of \"" + optimizationFunctions.get(i).getClass().getSimpleName() + "\" (ms)");
                    }
                    elapsedTimesDataset.add(populationData.getCurrentGeneration(), TimeUnit.NANOSECONDS.toMillis(populationData.getFrontingTime()), "Fronting Time (ms)");
                    elapsedTimesDataset.add(populationData.getCurrentGeneration(), TimeUnit.NANOSECONDS.toMillis(populationData.getTruncationTime()), "Truncation Time (ms)");
                    if (populationData.getPreviousObservationTime() > 0) {
                        elapsedTimesDataset.add(populationData.getCurrentGeneration() - 1, TimeUnit.NANOSECONDS.toMillis(populationData.getPreviousObservationTime()), "Previous Observation Time (ms)");
                    }
                });
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        addTab("Elapsed Times", elapsedTimesPanel);

        return this;
    }

    @SuppressWarnings("Duplicates")
    public <E> TabbedVisualizationWindow addJPPFJobProgressTab(EvolutionObservable<E> evolutionObservable, JPPFClient jppfClient) {
        JPanel jppfJobProgressPanel = new JPanel();
        jppfJobProgressPanel.setLayout(new BoxLayout(jppfJobProgressPanel, BoxLayout.Y_AXIS));
        Map<String, JProgressBar> progressBarMap = new HashMap<>();
        JLabel jppfJobProgressLabel = new JLabel("Currently Executing Tasks Queue:", SwingConstants.CENTER);
        jppfJobProgressPanel.add(jppfJobProgressLabel);
        jppfJobProgressLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        jppfJobProgressLabel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        jppfClient.addClientQueueListener(new ClientQueueListener() {
            @Override
            public void jobAdded(ClientQueueEvent event) {
                event.getJob().addJobListener(new JobListener() {
                    @Override
                    public void jobStarted(JobEvent event) {
                    }

                    @Override
                    public void jobEnded(JobEvent event) {
                        Thread t = new Thread(() -> {
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            EventQueue.invokeLater(() ->
                            {
                                JProgressBar progressBar = progressBarMap.get(event.getJob().getName());
                                if (progressBar.getPercentComplete() == 1)
                                    progressBar.setVisible(false);
                            });
                        });
                        t.setDaemon(true);
                        t.start();
                    }

                    @Override
                    public void jobDispatched(JobEvent event) {
                    }

                    @Override
                    public void jobReturned(JobEvent event) {
                        EventQueue.invokeLater(() ->
                        {
                            if (!progressBarMap.containsKey(event.getJob().getName())) {
                                progressBarMap.put(event.getJob().getName(), new JProgressBar());
                                jppfJobProgressPanel.add(progressBarMap.get(event.getJob().getName()));
                            }
                            JProgressBar progressBar = progressBarMap.get(event.getJob().getName());
                            progressBar.setVisible(true);
                            progressBar.setMinimum(0);
                            progressBar.setMaximum(event.getJob().getTaskCount());
                            progressBar.setValue(event.getJob().executedTaskCount());
                            progressBar.setStringPainted(true);
                            progressBar.setString(event.getJob().getName() + " - " + (int) (progressBar.getPercentComplete() * 100) + "%");
                            progressBar.setBorderPainted(true);
                            progressBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createBevelBorder(BevelBorder.LOWERED)));
                            //System.out.println(event.getJob().executedTaskCount() + "/" + event.getJob().getTaskCount() + " tasks completed for " + event.getJob().getName());
                        });
                    }
                });
            }

            @Override
            public void jobRemoved(ClientQueueEvent event) {
            }
        });

        addTab("JPPF Progress", jppfJobProgressPanel);

        return this;
    }

    @SuppressWarnings("Duplicates")
    public <E> TabbedVisualizationWindow addGenerationStatisticsTab(EvolutionObservable<E> evolutionObservable, List<StatisticFunction<E>> statisticFunctions) {
        YIntervalSeriesCollection priorStatisticsCollection = new YIntervalSeriesCollection();
        JFreeChart priorStatisticsChart = ChartFactory.createScatterPlot("Statistics", "Generation", "Statistics (max/mean/min)", priorStatisticsCollection, PlotOrientation.VERTICAL, true, false, false);
        priorStatisticsChart.getXYPlot().setRenderer(new XYErrorRenderer());
        ChartPanel priorStatisticsPanel = new ChartPanel(priorStatisticsChart);

        evolutionObservable.addObserver(populationData -> {
            try {
                EventQueue.invokeAndWait(() -> {
                    for (int i = 0; i < statisticFunctions.size(); i++) {
                        YIntervalSeries statisticSeries;
                        try {
                            statisticSeries = priorStatisticsCollection.getSeries(i);
                        } catch (IllegalArgumentException e) {
                            statisticSeries = new YIntervalSeries(statisticFunctions.get(i).getName());
                            priorStatisticsCollection.addSeries(statisticSeries);
                        }
                        DescriptiveStatistics scores = new DescriptiveStatistics(statisticFunctions.get(i).apply(populationData));
                        statisticSeries.add(populationData.getCurrentGeneration(), scores.getMean(), scores.getMin(), scores.getMax());
                    }
                });
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        addTab("Statistics", priorStatisticsPanel);

        return this;
    }

    public interface StatisticFunction<E> extends Function<PopulationData<E>, double[]> {
        String getName();

        @Override
        double[] apply(PopulationData<E> populationData);
    }
}
