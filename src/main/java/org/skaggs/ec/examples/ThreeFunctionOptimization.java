package org.skaggs.ec.examples;

import org.apache.commons.math3.util.FastMath;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.chart.factories.IChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Rectangle;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

/**
 * Created by Mitchell on 1/4/2016.
 */
public class ThreeFunctionOptimization {
    public static void main(String[] args) throws Exception {

        Coord3d[] points = new Coord3d[4];
        points[0] = new Coord3d(0, 0, 1);
        points[1] = new Coord3d(0, 1, 0);
        points[2] = new Coord3d(1, 0, 0);
        points[3] = new Coord3d(.5, .5, .5);
        int xMin = -10;
        int yMin = -10;
        int xMax = 10;
        int yMax = 10;
        int currentIndex = 0;
        Coord3d[] parabola = new Coord3d[(xMax - xMin) * (yMax - yMin)];
        for (int x = xMin; x < xMax; x++)
            for (int y = yMin; y < yMax; y++) {
                parabola[currentIndex] = new Coord3d(x, y, FastMath.pow(x, 2) + FastMath.pow(y, 2));
                currentIndex++;
            }

        Scatter scatter = new Scatter(parabola, new Color(.8f, .1f, 0f), 10);

        Chart chart = AWTChartComponentFactory.chart(Quality.Nicest, IChartComponentFactory.Toolkit.awt);
        chart.getScene().add(scatter);
        chart.getScene().add(new Scatter(points));

        ChartLauncher.openChart(chart, new Rectangle(0, 0, 400, 400), "Chart");

        Thread t = new Thread(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    Thread.sleep(14);
                    SwingUtilities.invokeAndWait(() -> {
                        Coord3d[] newPoints = new Coord3d[4];
                        newPoints[0] = new Coord3d(0, 0, 1);
                        newPoints[1] = new Coord3d(0, 1, 0);
                        newPoints[2] = new Coord3d(1, 0, 0);
                        newPoints[3] = new Coord3d(FastMath.sin(System.currentTimeMillis() / 1000d), .5, .5);
                        //scatter.setData(newPoints);

                    });
                } catch (InterruptedException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });
        t.setDaemon(true);
        //t.start();
    }
}
