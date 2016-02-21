package org.skaggs.ec.examples;

import org.apache.commons.math3.util.FastMath;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Mitchell on 1/4/2016.
 */
public final class ThreeFunctionOptimization {
    private ThreeFunctionOptimization() {
    }

    public static void main(String[] args) throws Exception {
        Coord3d[] newPoints = new Coord3d[9];
        newPoints[0] = new Coord3d(0, 0, 0);
        newPoints[1] = new Coord3d(0, 0, 1);
        newPoints[2] = new Coord3d(0, 1, 0);
        newPoints[3] = new Coord3d(0, 1, 1);
        newPoints[4] = new Coord3d(1, 0, 0);
        newPoints[5] = new Coord3d(1, 0, 1);
        newPoints[6] = new Coord3d(1, 1, 0);
        newPoints[7] = new Coord3d(1, 1, 1);
        newPoints[8] = new Coord3d(FastMath.sin(System.currentTimeMillis() / 1000d), .5, .5);

        Chart chart = AWTChartComponentFactory.chart(Quality.Nicest, IChartComponentFactory.Toolkit.newt);
        chart.getScene().add(new Scatter(newPoints, Color.BLACK, 10));

        chart.addMouseController();
        chart.addKeyController();

        JFrame frame = new JFrame("Test");
        frame.add((Component) chart.getCanvas());
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Thread t = new Thread(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        newPoints[0] = new Coord3d(0, 0, 0);
                        newPoints[1] = new Coord3d(0, 0, 1);
                        newPoints[2] = new Coord3d(0, 1, 0);
                        newPoints[3] = new Coord3d(0, 1, 1);
                        newPoints[4] = new Coord3d(1, 0, 0);
                        newPoints[5] = new Coord3d(1, 0, 1);
                        newPoints[6] = new Coord3d(1, 1, 0);
                        newPoints[7] = new Coord3d(1, 1, 1);
                        newPoints[8] = new Coord3d(FastMath.sin(System.currentTimeMillis() / 1000d), .5, .5);
                    });
                } catch (InterruptedException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                Thread.yield();
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
