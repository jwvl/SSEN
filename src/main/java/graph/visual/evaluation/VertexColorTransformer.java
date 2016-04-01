/**
 *
 */
package graph.visual.evaluation;

import grammar.dynamic.node.CostNode;
import learn.ViolatedCandidate;
import org.apache.commons.collections4.Transformer;

import java.awt.*;

/**
 * @author jwvl
 * @date Sep 9, 2015
 */
public class VertexColorTransformer implements Transformer<CostNode, Paint> {

    private ViolatedCandidate winner;
    private static Color best = new Color(148, 244, 45);
    private static Color worst = new Color(240, 54, 40);
    private static Color neutral = Color.cyan;

    /**
     * @param winner
     */
    public VertexColorTransformer(ViolatedCandidate winner) {
        this.winner = winner;
    }


    public Paint transform(CostNode i) {
        if (isInWinner(i)) {
            return best;
        }
        return worst;
    }


    /**
     * @param i
     * @return
     */
    private boolean isInWinner(CostNode i) {
        return winner.getCandidate().containsForm(i.getForm());
    }


    double ensureRange(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }
}
