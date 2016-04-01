/**
 *
 */
package grammar.dynamic;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import grammar.dynamic.node.CostNode;
import grammar.levels.LevelSpace;
import graph.visual.evaluation.VertexColorTransformer;
import learn.ViolatedCandidate;
import org.apache.commons.collections4.Transformer;

import java.util.Map;

/**
 * @author jwvl
 * @date Sep 8, 2015
 */
public class NetworkEvaluationGraph extends DirectedSparseGraph<CostNode, String> {

    private static final long serialVersionUID = 5356386916356120331L;
    private final LevelSpace levelSpace;
    private ViolatedCandidate winner;
    private VertexColorTransformer edgePainter;


    public NetworkEvaluationGraph(DynamicNetworkEvaluation evaluation) {
        this.levelSpace = evaluation.getGrammar().getLevelSpace();
        this.winner = evaluation.getWinner();
        Map<CostNode, CostNode> backPointers = null; // TODO re-fix this? used to be evaluation.getBackPointers();
        buildFromBackPointers(backPointers);
        this.edgePainter = new VertexColorTransformer(winner);

    }

    /**
     * @param backPointers
     */
    private void buildFromBackPointers(Map<CostNode, CostNode> backPointers) {
        for (CostNode node : backPointers.keySet()) {
            CostNode from = backPointers.get(node);
            addVertex(node);
            addVertex(from);
            String mappingString = String.format("%s > %s", from, node);
            try {
                addEdge(mappingString, from, node);
            } catch (IllegalArgumentException e) {
                System.out.println("Exception trying to add edge from " + from + " to " + node);
            }
        }
    }

    /**
     * @return the levelSpace
     */
    public LevelSpace getLevelSpace() {
        return levelSpace;
    }

    /**
     * @return the edgePainter
     */
    public VertexColorTransformer getEdgePainter() {
        return edgePainter;
    }


    public static Transformer<CostNode, String> LABEL_TRANSFORMER = new Transformer<CostNode, String>() {

        public String transform(CostNode arg0) {
            return arg0.getForm().toBracketedString();
        }
    };


}
