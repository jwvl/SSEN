/**
 *
 */
package eval.harmony;

import com.google.common.collect.Ordering;
import ranking.OldRankedConstraint;

/**
 * @author jwvl
 * @date 02/11/2014 Harmony is an abstract, orderable value that can be assigned
 * to some candidate.
 */
public abstract class Harmony<H extends Harmony<H>> {
    protected Harmony(int size, Iterable<OldRankedConstraint> violators) {
    }

    /**
     * Updates the values in a VectorHarmony object.
     *
     * @param constraintIndex Index of constraint in hierarchy
     * @param numViolations   Number of asterisks assigned.
     * @param rc              Constraint itself
     */
    public abstract void addViolation(OldRankedConstraint rc, int numViolations);

    public H createMerger(H b) {
        if (b == null)
            b = getNull();
        if (this == getNull()) {
            if (b == getNull())
                return getNull();
            return b.copy();
        } else if (b == getNull()) {
            return this.copy();
        }
        H result = this.copy();
        result.mergeHarmonies(b);
        return result;
    }

    public abstract H getInfinite();

    public abstract H getNull();

    protected Harmony() {
    }

    public abstract Ordering<H> getOrdering();

    public abstract void mergeHarmonies(H toAdd);

    /**
     * Does just what you think: makes a (deep) copy of this Harmony object.
     *
     * @return A copy of this Harmony
     */
    public abstract H copy();

    public abstract H getMin(H other);

    public abstract H initialize(int size,
                                 Iterable<OldRankedConstraint> constraints);

    public abstract String toString();

}
