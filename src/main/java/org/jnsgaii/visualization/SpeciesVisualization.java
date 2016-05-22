package org.jnsgaii.visualization;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Graphs;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jppf.JPPFException;
import org.jppf.client.JPPFClient;
import org.jppf.client.JPPFJob;
import org.jppf.node.protocol.AbstractTask;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Dimension;

/**
 * Created by Mitchell on 5/21/2016.
 */
public final class SpeciesVisualization {
    public static void main(String[] args) throws JPPFException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.WARN);

        //startVisualization();

        JPPFClient client = new JPPFClient("Test Client");
        JPPFJob job = new JPPFJob("Test Job");

        for (int i = 0; i < 100; i++) {
            job.add(new AbstractTask<String>() {
                @Override
                public void run() {
                    setResult(Thread.currentThread().getName());
                }
            });
        }

        job.setBlocking(false);

        client.submitJob(job);
        job.awaitResults().stream().forEach(task -> System.out.println(task.getResult()));

        client.close();
    }

    public static void startVisualization() {
        Graph<Integer, String> graph = Graphs.synchronizedUndirectedGraph(new UndirectedSparseMultigraph<>());

        graph.addVertex(0);
        graph.addVertex(1);
        graph.addEdge("", 0, 1);

        FRLayout<Integer, String> layout = new FRLayout<>(graph);
        layout.setSize(new Dimension(600, 600));
        layout.setRepulsionMultiplier(1);
        layout.setAttractionMultiplier(5);
        layout.initialize();

        VisualizationViewer<Integer, String> vv = new VisualizationViewer<>(layout);

        vv.setGraphMouse(new DefaultModalGraphMouse(.9f, 1 / .9f));

        JFrame frame = new JFrame("Simple Graph View");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
