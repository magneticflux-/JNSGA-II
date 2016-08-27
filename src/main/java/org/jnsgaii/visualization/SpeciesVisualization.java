package org.jnsgaii.visualization;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.OrderedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.MultiPickedState;
import org.apache.commons.math3.util.FastMath;
import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.operators.Speciator;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.RenderingHints;

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
        //layout.setRepulsionMultiplier(.1);
        //layout.setAttractionMultiplier(2);
        layout.setMaxIterations(10000);
        layout.initialize();

        //VisualizationViewer<Integer, Edge> vv = new VisualizationViewer<>(layout);
        //vv.getRenderingHints().remove(RenderingHints.KEY_ANTIALIASING);
        //vv.getRenderContext().setEdgeShapeTransformer(EdgeShape.line(graph));
        //vv.setGraphMouse(new DefaultModalGraphMouse(.9f, 1 / .9f));

        VisualizationViewer<Integer, Edge> vv = new VisualizationViewer<>(layout, layout.getSize());
        //vv.getModel().getRelaxer().setSleepTime(0);
        vv.getRenderContext().setPickedVertexState(new MultiPickedState<>());
        vv.getRenderContext().setPickedEdgeState(new MultiPickedState<>());
        vv.getRenderingHints().remove(RenderingHints.KEY_ANTIALIASING);
        vv.getRenderContext().setEdgeStrokeTransformer(input -> new BasicStroke(FastMath.abs((float) (input.distance))));
        vv.setGraphMouse(new DefaultModalGraphMouse(.9f, 1 / .9f));

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(vv);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

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

