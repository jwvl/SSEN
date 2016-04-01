package eval.harmony;

import com.google.common.collect.Ordering;
import ranking.OldRankedConstraint;
import ranking.SortingValues;
import util.collections.AutoSortedList;

import java.util.ArrayList;
import java.util.List;

public class SortedHarmony extends Harmony<SortedHarmony> {
    private static Strategy strategy = Strategy.ARRAY;

    private enum Strategy {ARRAY, LIST, MULTISET}

    private final static SortedHarmony INFINITE = new InfiniteHarmony(SortingValues.INFINITY);
    private final static SortedHarmony NULL = createNull();


    private static class InfiniteHarmony extends SortedHarmony {
        private InfiniteHarmony(SortingValues s) {
            super(SortedListFactory.createInfinite());
        }

        public String toString() {
            return "∞";
        }
    }

    private AutoSortedList<SortingValues> contents;


    private static Ordering<SortedHarmony> ordering = new Ordering<SortedHarmony>() {

        @Override
        public int compare(SortedHarmony arg0, SortedHarmony arg1) {
            if (arg0 == INFINITE) {
                if (arg1 == INFINITE)
                    return 0;
                return 1;
            } else if (arg1 == INFINITE) {
                return -1;
            }
            return arg0.contents.getOrdering().compare(arg0.contents, arg1.contents);
        }

    };

    /**
     * @param c SortingValues to put as contents.
     */
    public SortedHarmony(AutoSortedList<SortingValues> contents) {
        this.contents = contents;
    }

    /**
     * Only used to create null object
     */
    public SortedHarmony() {
    }

    private static SortedHarmony createNull() {
        return new SortedHarmony();
    }

    /* (non-Javadoc)
     * @see eval.harmony.Harmony#addViolation(con.RankedConstraint, int, int)
     */
    @Override
    public void addViolation(OldRankedConstraint rc, int numViolations) {
        for (int i = 0; i < numViolations; i++) {
            this.contents.add(rc.getSortingValues());
        }


    }

    /* (non-Javadoc)
     * @see eval.harmony.Harmony#getInfinite()
     */
    @Override
    public SortedHarmony getInfinite() {
        return INFINITE;
    }

    /* (non-Javadoc)
     * @see eval.harmony.Harmony#getOrdering()
     */
    @Override
    public Ordering<SortedHarmony> getOrdering() {
        return ordering;
    }

    /* (non-Javadoc)
     * @see eval.harmony.Harmony#mergeHarmonies(eval.harmony.Harmony)
     */
    @Override
    public void mergeHarmonies(SortedHarmony o) {
        contents.abstractMergeWith(o.contents);

    }

    /* (non-Javadoc)
     * @see eval.harmony.Harmony#copy()
     */
    @Override
    public SortedHarmony copy() {
        return new SortedHarmony(contents.copy());
    }

    public AutoSortedList<SortingValues> getContents() {
        return contents;
    }

    @Override
    public SortedHarmony getMin(SortedHarmony other) {
        if (this == NULL) {
            if (other == NULL)
                return getNull();
            return other;
        } else if (other == NULL)
            return this;
        return ordering.min(this, other);
    }

    public SortedHarmony initialize(int size,
                                    Iterable<OldRankedConstraint> violators) {
        List<SortingValues> values = new ArrayList<SortingValues>();
        for (OldRankedConstraint rc : violators) {
            values.add(rc.getSortingValues());
        }
        if (strategy == Strategy.ARRAY) {
            contents = SortedListFactory.createArray(values);
        } else if (strategy == Strategy.LIST) {
            contents = SortedListFactory.createList(values);
        } else {
            contents = SortedListFactory.createMultiset(values);
        }
        return new SortedHarmony(contents);

    }

    @Override
    public SortedHarmony getNull() {
        return NULL;
    }

    @Override
    public String toString() {
        return contents.getListedContents("Contents");
    }


}
