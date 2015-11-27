package org.skaggs;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.skaggs.ec.properties.Key;
import org.skaggs.ec.properties.Properties;

import javax.swing.JFrame;

public class Main {

    public static void main(String[] args) {
        Properties p = new Properties().setInt(Key.IntKey.INT_POPULATION, 500).lock();
        System.out.println("Hello world!");
        System.out.println(p);
        DefaultKeyedValues keyedValues = new DefaultKeyedValues();
        keyedValues.addValue("Value 1", Math.random());
        keyedValues.addValue("Value 2", Math.random());
        keyedValues.addValue("Value 3", Math.random());
        keyedValues.addValue("Value 4", Math.random());
        PieDataset d = new DefaultPieDataset(keyedValues);
        JFreeChart chart = ChartFactory.createPieChart("Test", d, true, true, false);
        ChartPanel panel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setSize(400, 400);
        frame.setVisible(true);
    }
}
