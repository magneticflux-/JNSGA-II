package org.jnsgaii;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        UpdatableHistogramDataset dataset = new UpdatableHistogramDataset();

        dataset.addSeries("Test 1", IntStream.range(0, 100000).mapToDouble(value -> ThreadLocalRandom.current().nextGaussian()).toArray(), 100);

        Thread t = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dataset.removeAllSeries();
        });
        t.setDaemon(true);
        t.start();
        //dataset.addSeries("Test 2", IntStream.range(0, 100000).mapToDouble(value -> ThreadLocalRandom.current().nextGaussian()).toArray(), 100);

        JFreeChart chart = ChartFactory.createHistogram("Title", "X Axis", "Y Axis", dataset, PlotOrientation.VERTICAL, false, true, false);
        ChartPanel panel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setSize(800, 800);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
