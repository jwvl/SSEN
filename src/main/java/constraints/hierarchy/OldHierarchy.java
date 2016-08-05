/**
 *
 */
package constraints.hierarchy;

import candidates.Candidate;
import com.google.common.collect.Ordering;
import constraints.Constraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A Hierarchy is an ordering over a set of constraints. More or less a wrapper
 * for a List of RankedConstraint objects, but with some added functionality for
 * functions that are used often.
 *
 * @author jwvl
 * @date Nov 17, 2014
 */
public class OldHierarchy implements Iterable<OldRankedConstraint> {
    private List<OldRankedConstraint> contents;
    private Ordering<OldRankedConstraint> ordering;
    private static Ordering<OldRankedConstraint> defaultOrdering = OldRankedConstraint.DisharmonyComparator;

    public static OldHierarchy defaultFromCon(Con con) {
        int size = con.size();
        OldHierarchy result = new OldHierarchy(size);
        for (Constraint c : con) {
            result.contents.add(OldRankedConstraint.createDefault(c));
        }
        return result;
    }

    public static OldHierarchy createEmpty() {
        return new OldHierarchy(0);
    }

    public static OldHierarchy fromRankedList(List<OldRankedConstraint> l) {
        OldHierarchy result = new OldHierarchy(l.size());
        result.contents = new ArrayList<OldRankedConstraint>(l);
        return result;

    }

    private OldHierarchy(int size) {
        contents = new ArrayList<OldRankedConstraint>(size);
    }

    public Constraint getConstraintByIndex(int index) {
        return contents.get(index).getConstraint();
    }

    public OldRankedConstraint getRankedByIndex(int index) {
        return contents.get(index);
    }

    /**
     * @return Number of constraints in the hierarchy
     */
    public int getSize() {
        return contents.size();
    }

    public int[] getViolationVector(Candidate can) {
        int[] result = new int[getSize()];
        int count = 0;
        for (OldRankedConstraint rc : contents) {
            Constraint c = rc.getConstraint();
            result[count++] = c.getNumViolations(can);
        }
        return result;
    }

    public Iterator<OldRankedConstraint> iterator() {
        return contents.iterator();
    }

    /**
     *
     */
    public void printOrdered() {
        int count = 0;
        for (OldRankedConstraint rc : this) {
            System.out.println((count++) + ": " + rc.toStringWithRankings());
        }

    }

    public void sort() {
        if (ordering == null)
            ordering = defaultOrdering;
        sort(ordering);
    }

    public void sort(Ordering<OldRankedConstraint> o) {
        Collections.sort(contents, o);
        for (int i = 0; i < contents.size(); i++) {
            // TODO make this work again? For now, just ignoring it
            // contents.get(i).setRankingIndex(i);
        }
    }

    public List<OldRankedConstraint> getContents() {
        return contents;
    }

}
