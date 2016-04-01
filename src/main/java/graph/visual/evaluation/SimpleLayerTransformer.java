package graph.visual.evaluation;

import forms.Form;
import forms.GraphForm;
import grammar.dynamic.DynamicNetworkEvaluation;
import grammar.dynamic.NetworkEvaluationGraph;
import grammar.dynamic.node.CostNode;
import grammar.levels.LevelSpace;
import graph.visual.DoubleCoordinate;
import org.apache.commons.collections4.Transformer;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * This class is responsible for assigning the forms in a layered graph a
 * 'location' in terms of left-right and top-bottom order. These locations can
 * then, for instance, be transformed to pixels on a screen.
 *
 * @author Jan-Willem van Leussen, Nov 24, 2014
 */
public class SimpleLayerTransformer implements Transformer<CostNode, Point2D> {
    int graphWidth;
    Dimension myDimension;
    LevelSpace levelSpace;
    DynamicNetworkEvaluation evaluation;
    List<TreeSet<CostNode>> positions;

    public static SimpleLayerTransformer createInstance(NetworkEvaluationGraph graph, Dimension d) {
        return new SimpleLayerTransformer(graph, graph.getLevelSpace(), d);
    }

    private SimpleLayerTransformer(NetworkEvaluationGraph graph, LevelSpace space, Dimension d) {
        myDimension = d;
        levelSpace = space;
        graphWidth = space.getSize();
        positions = setPositions(graph);
    }

	/*
     * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.commons.collections15.Transformer#transform(java.lang.Object)
	 */

    /**
     * @param evaluation2
     * @return
     */
    private List<TreeSet<CostNode>> setPositions(NetworkEvaluationGraph evaluation2) {
        List<TreeSet<CostNode>> result = new ArrayList<TreeSet<CostNode>>(graphWidth);
        for (int i = 0; i < graphWidth; i++) {
            result.add(new TreeSet<CostNode>());
        }
        for (CostNode node : evaluation2.getVertices()) {
            Form form = node.getForm();
            int levelNumber = levelSpace.getLevelIndex(form.getLevel());
            if (levelNumber >= 0 && levelNumber < graphWidth) {
                result.get(levelNumber).add(node);
            }
        }
        return result;
    }

    public Point2D transform(CostNode cd) {
        DoubleCoordinate frc = null;
        Form f = cd.getForm();
        if (f instanceof GraphForm) {
            GraphForm gf = (GraphForm) f;
            if (gf.isSink()) {
                frc = new DoubleCoordinate(0.9, 0.5);
            } else {
                frc = new DoubleCoordinate(0.1, 0.5);
            }
        } else {
            int levelInSpace = levelSpace.getLevelIndex(f.getLevel());
            TreeSet<CostNode> levelSet = positions.get(levelInSpace);
            int yPos = levelSet.headSet(cd).size() + 1;
            double yTotal = levelSet.size() + 1;
            frc = new DoubleCoordinate(levelInSpace / (double) graphWidth, yPos / yTotal);
        }
        int width = myDimension.width;
        int height = myDimension.height;
        Point2D result = new Point2D.Double(frc.x() * width, frc.y() * height);
        return result;
    }

}
