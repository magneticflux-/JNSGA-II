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

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

/**
 * Created by Mitchell Skaggs on 3/30/2016.
 *
 * @deprecated Soon to be completely replicated modularly by {@link TabbedVisualizationWindow}
 */
@Deprecated
public final class DefaultVisualization {

    private DefaultVisualization() {
    }

    public static <E> void displayGenerationGraph(String[] aspectDescriptions, String[] scoreDescriptions, Stream<PopulationData<E>> generationData) {
        JFrame window = new JFrame("Generations");
        GroupLayout layout = new GroupLayout(window.getContentPane());
        window.getContentPane().setLayout(layout);

        YIntervalSeriesCollection averageAspectCollection = new YIntervalSeriesCollection();
        JFreeChart averageAspectChart = ChartFactory.createScatterPlot("Average Aspect Values", "Generation", "Aspect Values", averageAspectCollection, PlotOrientation.VERTICAL, true, false, false);
        averageAspectChart.getXYPlot().setRenderer(new XYErrorRenderer());
        ChartPanel averageAspectPanel = new ChartPanel(averageAspectChart);

        YIntervalSeriesCollection averageScoreCollection = new YIntervalSeriesCollection();
        JFreeChart averageScoreChart = ChartFactory.createScatterPlot("Max Scores", "Generation", "Scores", averageScoreCollection, PlotOrientation.VERTICAL, true, false, false);
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

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);

        //Executor forkJoinPool = new ForkJoinPool(8);
        //forkJoinPool.execute(() ->)
        generationData.forEach(populationData -> EventQueue.invokeLater(() -> {

            updateAverageAspectCollection(averageAspectCollection, aspectDescriptions, populationData);

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
                double scoreSummaryMax = scoreSummary.getMax();
                double scoreSummaryMin = scoreSummary.getMin();
                double scoreSummaryMedian = scoreSummary.getPercentile(.5);
                scoreSeries.add(populationData.getCurrentGeneration(), scoreSummaryMin, scoreSummaryMedian, scoreSummaryMax);
            }
        }));
    }

    /**
     * @deprecated Use {@link TabbedVisualizationWindow} instead.
     */
    @SuppressWarnings("Duplicates")
    @Deprecated
    public static <E> void startInterface(@SuppressWarnings("TypeMayBeWeakened") DefaultOperator<E> operator, List<OptimizationFunction<E>> optimizationFunctions, List<Computation<E, ?>> computations, EvolutionObservable<E> evolutionObservable, JPPFClient jppfClient) {
        Box histogramPanel = new Box(BoxLayout.Y_AXIS);
        JScrollPane histogramScrollPane = new JScrollPane(histogramPanel);
        histogramScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);

        YIntervalSeriesCollection priorScoresCollection = new YIntervalSeriesCollection();
        JFreeChart priorScoresChart = ChartFactory.createScatterPlot("Prior Scores", "Generation", "Scores (max/mean/min)", priorScoresCollection, PlotOrientation.VERTICAL, true, false, false);
        priorScoresChart.getXYPlot().setRenderer(new XYErrorRenderer());
        ChartPanel priorScoresPanel = new ChartPanel(priorScoresChart);

        YIntervalSeriesCollection averageAspectCollection = new YIntervalSeriesCollection();
        JFreeChart averageAspectChart = ChartFactory.createScatterPlot("Average Aspect Values", "Generation", "Aspect Values (25/50/75)", averageAspectCollection, PlotOrientation.VERTICAL, true, false, false);
        averageAspectChart.getXYPlot().setRenderer(new XYErrorRenderer());
        ChartPanel averageAspectPanel = new ChartPanel(averageAspectChart);

        CategoryTableXYDataset elapsedTimesDataset = new CategoryTableXYDataset();
        NumberAxis elapsedTimesChartDomainAxis = new NumberAxis("Generation");
        elapsedTimesChartDomainAxis.setAutoRangeIncludesZero(false);
        NumberAxis elapsedTimesChartRangeAxis = new NumberAxis("Elapsed Time (ms)");
        XYPlot elapsedTimesChartPlot = new XYPlot(elapsedTimesDataset, elapsedTimesChartDomainAxis, elapsedTimesChartRangeAxis, new StackedXYBarRenderer(.25));
        elapsedTimesChartPlot.setOrientation(PlotOrientation.VERTICAL);
        JFreeChart elapsedTimesChart = new JFreeChart("Elapsed Times", JFreeChart.DEFAULT_TITLE_FONT, elapsedTimesChartPlot, true);//ChartFactory.createStackedBarChart3D("Elapsed Times", "Generation", "Elapsed Time (ms)", elapsedTimesDataset, PlotOrientation.VERTICAL, true, false, false);
        StandardChartTheme.createJFreeTheme().apply(elapsedTimesChart);
        ChartPanel elapsedTimesPanel = new ChartPanel(elapsedTimesChart);

        JPanel jppfJobProgressPanel = new JPanel();
        jppfJobProgressPanel.setLayout(new BoxLayout(jppfJobProgressPanel, BoxLayout.Y_AXIS));
        Map<String, JProgressBar> progressBarMap = new HashMap<>();
        JLabel jppfJobProgressLabel = new JLabel("Currently Executing Tasks Queue:", SwingConstants.CENTER);
        jppfJobProgressPanel.add(jppfJobProgressLabel);
        jppfJobProgressLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        jppfJobProgressLabel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JFrame windowFrame = new JFrame("Evolutionary Algorithm");
        JPanel mainPanel = new JPanel();
        windowFrame.setLayout(new BorderLayout());
        windowFrame.add(mainPanel, BorderLayout.CENTER);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(jTabbedPane, BorderLayout.CENTER);

        jTabbedPane.addTab("Current Score Distributions", histogramScrollPane);
        jTabbedPane.addTab("Prior Scores", priorScoresPanel);
        jTabbedPane.addTab("Median Aspect Values", averageAspectPanel);
        jTabbedPane.addTab("Elapsed Times", elapsedTimesPanel);
        jTabbedPane.addTab("JPPF Job Progress", jppfJobProgressPanel);

        String[] aspectDescriptions = operator.getAspectDescriptions();

        UpdatableHistogramDataset[] datasets = new UpdatableHistogramDataset[optimizationFunctions.size()];
        for (int i = 0; i < datasets.length; i++) {
            datasets[i] = new UpdatableHistogramDataset();
            ChartPanel panel = new ChartPanel(ChartFactory.createHistogram(optimizationFunctions.get(i).getClass().getSimpleName(), "Score", "Frequency", datasets[i], PlotOrientation.VERTICAL, true, false, false));
            histogramPanel.add(panel);
        }

        //noinspection MagicNumber
        windowFrame.setSize(1000, 1000);
        //noinspection MagicNumber
        windowFrame.setLocation(0, 0);
        windowFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        windowFrame.setVisible(true);

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

        evolutionObservable.addObserver(populationData -> { // Current score histograms
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

        evolutionObservable.addObserver(populationData -> { // Prior scores
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

        evolutionObservable.addObserver(populationData -> { // Average Aspects
            try {
                EventQueue.invokeAndWait(() -> updateAverageAspectCollection(averageAspectCollection, aspectDescriptions, populationData));
            } catch (InterruptedException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });

        evolutionObservable.addObserver(populationData -> { // Elapsed Times
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
    }

    @SuppressWarnings("Duplicates")
    private static <E> void updateAverageAspectCollection(YIntervalSeriesCollection averageAspectCollection, String[] aspectDescriptions, PopulationData<E> populationData) {
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
    }
}
