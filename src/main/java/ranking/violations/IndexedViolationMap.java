/**
 *
 */
package ranking.violations;

import gen.mapping.FormMapping;
import graph.LayeredGraph;
import ranking.OldHierarchy;
import ranking.constraints.Constraint;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public class IndexedViolationMap extends ViolationMap<IndexedViolation> {


    public static ViolationMap createEdgeInstance(LayeredGraph lg, OldHierarchy h) {
        List<FormMapping> allEdges = new ArrayList<FormMapping>();
        allEdges.addAll(lg.getGraph().getEdges());
        return new IndexedViolationMap(allEdges, h);
    }

    public static ViolationMap createInstance(LayeredGraph lg, OldHierarchy h) {
        List<FormMapping> allTransgressors = new ArrayList<FormMapping>();
        allTransgressors.addAll(lg.getGraph().getEdges());
//		allTransgressors.addAll(lg.getGraph().getVertices());
        return new IndexedViolationMap(allTransgressors, h);
    }

    public static ViolationMap createInstance(List<FormMapping> tList, OldHierarchy h) {
        return new IndexedViolationMap(tList, h);
    }

    public static ViolationMap createVertexInstance(LayeredGraph lg, OldHierarchy h) {
        List<FormMapping> allVertices = new ArrayList<FormMapping>();
        //	allVertices.addAll(lg.getGraph().getVertices());
        return new IndexedViolationMap(allVertices, h);
    }

    private IndexedViolationMap(List<FormMapping> tList, OldHierarchy h) {
        super(tList, h);
    }

    /*
     * (non-Javadoc)
     *
     * @see con.violations.ViolationMap#fill(graph.LayeredGraph, con.Hierarchy)
     */
    @Override
    public void fill(List<FormMapping> tList, OldHierarchy h) {

        for (FormMapping t : tList) {
            for (int i = 0; i < h.getSize(); i++) {
                Constraint c = h.getConstraintByIndex(i);
                int numViolations = c.getNumViolations(t);
                if (numViolations > 0) {
                    add(t, IndexedViolation.createInstance(i, numViolations));
                }
            }
        }

    }

}
