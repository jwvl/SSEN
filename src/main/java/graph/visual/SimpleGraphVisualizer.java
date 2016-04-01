package graph.visual;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

import java.awt.*;

/**
 * Created by janwillem on 30/03/16.
 */
public class SimpleGraphVisualizer<F, G> {
    private final Layout<F, G> layout;
    private final BasicVisualizationServer<F, G> visualizationServer;


    public SimpleGraphVisualizer(Graph<F, G> graph) {
        layout = new CircleLayout<>(graph);
        Dimension firstDimension = new Dimension(400, 400);
        Dimension secondDimension = new Dimension(450, 450);
        layout.setSize(firstDimension);
        visualizationServer = new BasicVisualizationServer<>(layout);
        visualizationServer.setPreferredSize(secondDimension);
        visualizationServer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        visualizationServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        visualizationServer.getRenderContext().setEdgeLabelTransformer(g -> "");
        visualizationServer.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.RED));

    }


    public BasicVisualizationServer<F, G> getVisualizationViewer() {
        return visualizationServer;
    }

}
