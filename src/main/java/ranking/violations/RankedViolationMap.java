/**
 *
 */
package ranking.violations;

import gen.mapping.FormMapping;
import ranking.OldHierarchy;
import ranking.OldRankedConstraint;

import java.util.Collections;
import java.util.List;

/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public class RankedViolationMap extends ViolationMap<RankedViolation> {


    protected static ViolationMap<RankedViolation> createInstance(List<FormMapping> lt, OldHierarchy h) {
        return new RankedViolationMap(lt, h);
        // TODO Auto-generated constructor stub
    }

    private RankedViolationMap(List<FormMapping> lt, OldHierarchy h) {
        super(lt, h);
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
                OldRankedConstraint rc = h.getRankedByIndex(i);
                int numViolations = rc.getNumViolations(t);
                if (numViolations > 0) {
                    add(t, RankedViolation.createInstance(rc, numViolations));
                }
            }
            Collections.sort(get(t));
        }

    }

}
