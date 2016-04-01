package graph.visual;

import edu.uci.ics.jung.visualization.BasicVisualizationServer;

import javax.swing.*;
import java.awt.*;

/**
 * Created by janwillem on 30/03/16.
 */
public class SimpleGraphWindow<F, G> extends JFrame {
    private final BasicVisualizationServer<F, G> visualizationViewer;

    public SimpleGraphWindow(SimpleGraphVisualizer<F, G> simpleGraphVisualizer) throws HeadlessException {
        super("Graph window");
        this.visualizationViewer = simpleGraphVisualizer.getVisualizationViewer();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(visualizationViewer);
        pack();
        setVisible(true);
    }
}
