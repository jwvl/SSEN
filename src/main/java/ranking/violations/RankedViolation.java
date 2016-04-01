/**
 *
 */
package ranking.violations;

import ranking.OldRankedConstraint;
import ranking.constraints.Constraint;

/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public class RankedViolation implements Violation, Comparable<RankedViolation> {
    private final OldRankedConstraint rc;
    private final int value;

    public static RankedViolation createInstance(OldRankedConstraint rc, int value) {
        RankedViolation result = new RankedViolation(rc, value);
        return result;
    }

    /**
     * @param c
     * @param value
     */
    private RankedViolation(OldRankedConstraint rc, int value) {
        this.rc = rc;
        this.value = value;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(RankedViolation rv) {
        int disharmonyComparison = OldRankedConstraint.DisharmonyComparator.compare(rc, rv.rc);
        if (disharmonyComparison != 0)
            return disharmonyComparison;
        else return value - rv.value;
    }

    public int getValue() {
        return value;
    }


    public OldRankedConstraint getRankedConstraint() {
        return rc;
    }

    /*
     * (non-Javadoc)
     *
     * @see con.violations.Violation#asArrayString()
     */
    @Override
    public String toString() {
        String dh = rc.getFormattedDisharmony();
        StringBuffer result = new StringBuffer(String.valueOf(dh));
        for (int i = 1; i < value; i++) {
            result.append(", ");
            result.append(dh);
        }
        return result.toString();
    }

    /* (non-Javadoc)
     * @see ranking.violations.Violation#getConstraint()
     */
    @Override
    public Constraint getConstraint() {
        return rc.getConstraint();
    }
}
