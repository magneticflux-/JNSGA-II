package org.jnsgaii.visualization;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.OrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;
import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.operators.Speciator;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Mitchell on 5/21/2016.
 */
public final class SpeciesVisualization {
    public static <E> void startVisualization(FrontedPopulation<E> frontedPopulation, Speciator<E> speciator, int generation) {
        Graph<Integer, Edge> graph = Graphs.synchronizedGraph(new OrderedSparseMultigraph<>());

        System.out.println("Starting initialization...");
        for (int i = 0; i < frontedPopulation.getPopulation().size(); i++) {
            graph.addVertex(i);
        }

        for (int outer = 0; outer < frontedPopulation.getPopulation().size(); outer++) {
            for (int inner = outer + 1; inner < frontedPopulation.getPopulation().size(); inner++) {
                if (speciator.apply(frontedPopulation.getPopulation().get(outer), frontedPopulation.getPopulation().get(inner))) {
                    Edge edge = new Edge(speciator.getDistance(frontedPopulation.getPopulation().get(outer), frontedPopulation.getPopulation().get(inner)), outer, inner);
                    graph.addEdge(edge, outer, inner);
                }
            }
        }
        System.out.println("Finished initialization!");

        FRLayout<Integer, Edge> layout = new FRLayout<>(graph);
        layout.setSize(new Dimension(10000, 10000));
        layout.setRepulsionMultiplier(.1);
        //layout.setAttractionMultiplier(2);
        layout.setMaxIterations(5000);
        layout.initialize();

        //VisualizationViewer<Integer, Edge> vv = new VisualizationViewer<>(layout);
        //vv.getRenderingHints().remove(RenderingHints.KEY_ANTIALIASING);
        //vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(graph));
        //vv.setGraphMouse(new DefaultModalGraphMouse(.9f, 1 / .9f));

        System.out.println("Starting layout...");
        VisualizationImageServer<Integer, Edge> visualizationImageServer = new VisualizationImageServer<>(layout, layout.getSize());
        visualizationImageServer.getModel().getRelaxer().setSleepTime(0);
        visualizationImageServer.getRenderContext().setPickedVertexState(new MultiPickedState<>());
        visualizationImageServer.getRenderContext().setPickedEdgeState(new MultiPickedState<>());
        while (!layout.done()) layout.step();
        System.out.println("Finished layout!");
        System.out.println("Starting rendering...");
        BufferedImage result = (BufferedImage) visualizationImageServer.getImage(new Point2D.Double(0, 0), layout.getSize());
        System.out.println("Finished rendering!");

        /*
        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //g.drawImage(result, 0, 0, this);
            }
        });
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        */

        System.out.println("Starting write...");
        try {
            File outputFile = new File(generation + ".jpg");
            ImageIO.write(result, "jpg", outputFile);
        } catch (IOException e) {
        }
        System.out.println("Finished write!");
    }

    private static final class Edge {
        final double distance;
        final int start, end;

        Edge(double distance, int start, int end) {
            this.distance = distance;
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (Double.compare(edge.distance, distance) != 0) return false;
            if (start != edge.start) return false;
            return end == edge.end;

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(distance);
            result = (int) (temp ^ (temp >>> 32));
            result = 31 * result + start;
            result = 31 * result + end;
            return result;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "distance=" + distance +
                    ", start=" + start +
                    ", end=" + end +
                    '}';
        }
    }
}

