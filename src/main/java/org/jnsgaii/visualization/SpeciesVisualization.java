package org.jnsgaii.visualization;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import org.jnsgaii.multiobjective.population.FrontedPopulation;
import org.jnsgaii.operators.Speciator;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;

/**
 * Created by Mitchell on 5/21/2016.
 */
public final class SpeciesVisualization {

    public static <E> void startVisualization(FrontedPopulation<E> frontedPopulation, Speciator<E> speciator) {
        Graph<Integer, Double> graph = Graphs.synchronizedUndirectedGraph(new UndirectedSparseMultigraph<>());

        for (int i = 0; i < frontedPopulation.getPopulation().size(); i++) {
            graph.addVertex(i);
        }

        for (int outer = 0; outer < frontedPopulation.getPopulation().size(); outer++) {
            for (int inner = outer; inner < frontedPopulation.getPopulation().size(); inner++) {
                if (speciator.apply(frontedPopulation.getPopulation().get(outer), frontedPopulation.getPopulation().get(inner))) {
                    graph.addEdge(speciator.getDistance(frontedPopulation.getPopulation().get(outer), frontedPopulation.getPopulation().get(inner)), outer, inner);
                }
            }
        }

        FRLayout<Integer, Double> layout = new FRLayout<>(graph);
        layout.setSize(new Dimension(600, 600));
        layout.setRepulsionMultiplier(1);
        layout.setAttractionMultiplier(5);
        layout.initialize();

        VisualizationViewer<Integer, Double> vv = new VisualizationViewer<>(layout);

        vv.setGraphMouse(new DefaultModalGraphMouse(.9f, 1 / .9f));

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
