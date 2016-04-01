/**
 *
 */
package eval.harmony;

import com.google.common.collect.Ordering;
import ranking.OldRankedConstraint;

/**
 * Implementation of Optimality-Theoretic harmony, as defined in Prince &
 * Smolensky (1993). OTHarmony objects are ordered lexicographically through
 * their violation vectors.
 *
 * @author jwvl
 * @date Dec 3, 2014
 */
public class OTHarmony extends Harmony<OTHarmony> {
    private static final OTHarmony INFINITE = createInfinite();
    private static final OTHarmony NULL = createNull();

    ViolationVector contents;

    private static Ordering<OTHarmony> otOrdering = new Ordering<OTHarmony>() {

        @Override
        public int compare(OTHarmony arg0, OTHarmony arg1) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == NULL)
                return -1;
            else if (arg1 == NULL)
                return 1;

            if (arg0 == INFINITE)
                return 1;
            else if (arg1 == INFINITE)
                return -1;

            return arg0.contents.compareTo(arg1.contents);
        }

    };

    private OTHarmony(int size) {
        contents = ViolationVector.createIntVector(size);
    }

    private static OTHarmony createNull() {
        return new OTHarmony();
    }

    @Override
    public OTHarmony getNull() {
        return NULL;
    }

    protected OTHarmony() {

    }

    /*
     * (non-Javadoc)
     *
     * @see eval.Harmony#addViolation(con.RankedConstraint, int, int)
     */
    @Override
    public void addViolation(OldRankedConstraint rc, int numViolations) {
        //increaseValues(rc.getRankingIndex(), numViolations);
        // TODO careful! This class will not work
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.Harmony#getWorst()
     */
    @Override
    public OTHarmony getInfinite() {
        return INFINITE;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.Harmony#getOrdering()
     */
    @Override
    public Ordering<OTHarmony> getOrdering() {
        return otOrdering;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.Harmony#setValues(int, int)
     */
    public void increaseValues(int constraintIndex, int numViolations) {
        for (int i = 0; i < numViolations; i++) {
            contents.incrementAt(constraintIndex);
        }

    }

    /**
     * @return
     */
    private static OTHarmony createInfinite() {
        OTHarmony result = new OTHarmony(1);
        result.increaseValues(0, Integer.MAX_VALUE);
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.Harmony#copy()
     */
    @Override
    public OTHarmony copy() {
        OTHarmony result = new OTHarmony(contents.size());
        result.contents = this.contents.copy();
        return result;
    }

    @Override
    public void mergeHarmonies(OTHarmony toAdd) {
        this.contents.mergeWith(toAdd.contents);

    }

    @Override
    public OTHarmony getMin(OTHarmony other) {
        if (this == NULL) {
            if (other == NULL)
                return getNull();
            return other;
        } else if (other == NULL)
            return this;
        return otOrdering.min(this, other);
    }

    public OTHarmony initialize(int size, Iterable<OldRankedConstraint> violators) {
        OTHarmony result = new OTHarmony(size);
        for (OldRankedConstraint rc : violators) {
            result.addViolation(rc, 1);
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see eval.harmony.Harmony#toString()
     */
    @Override
    public String toString() {
        if (this == NULL)
            return ("[0]");
        else if (this == INFINITE)
            return "oo";
        return contents.toString();
    }

}
