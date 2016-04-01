/**
 *
 */
package eval.harmony;

import eval.harmony.autosort.SortedDoublesArray;
import eval.harmony.autosort.StratifiedDouble;
import ranking.OldRankedConstraint;

import java.util.Collection;

/**
 * @author jwvl
 * @date Aug 15, 2015
 */
public class SimpleOTHarmony implements Cost<SimpleOTHarmony> {
    private final SortedDoublesArray sortedDoubles;

    private SimpleOTHarmony(SortedDoublesArray sortedDoubles) {
        this.sortedDoubles = sortedDoubles;
    }

    public static SimpleOTHarmony initializeEmpty() {
        StratifiedDouble[] emtpy = new StratifiedDouble[0];
        SortedDoublesArray toAdd = SortedDoublesArray.createInstance(emtpy);
        return new SimpleOTHarmony(toAdd);
    }


    public SimpleOTHarmony merge(Collection<OldRankedConstraint> constraints) {
        SortedDoublesArray newArray = SortedDoublesArray.createFromRankedConstraints(constraints);
        SortedDoublesArray mergedDoubles = sortedDoubles.mergeWith(newArray);
        return new SimpleOTHarmony(mergedDoubles);
    }

    public SimpleOTHarmony mergeWithDisharmonies(Collection<StratifiedDouble> disharmonies) {
        SortedDoublesArray newArray = SortedDoublesArray.createFromDisharmonies(disharmonies);
        SortedDoublesArray mergedDoubles = sortedDoubles.mergeWith(newArray);
        return new SimpleOTHarmony(mergedDoubles);
    }

    /* (non-Javadoc)
     * @see eval.harmony.Harmony#toString()
     */
    @Override
    public String toString() {
        return sortedDoubles.toString();
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SimpleOTHarmony o) {
        return sortedDoubles.compareTo(o.sortedDoubles);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((sortedDoubles == null) ? 0 : sortedDoubles.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SimpleOTHarmony))
            return false;
        SimpleOTHarmony other = (SimpleOTHarmony) obj;
        if (sortedDoubles == null) {
            if (other.sortedDoubles != null)
                return false;
        } else if (!sortedDoubles.equals(other.sortedDoubles))
            return false;
        return true;
    }

    /**
     * @return
     */
    public double leftmost() {
        return sortedDoubles.getLeftmostValue().getValue();
    }


    @Override
    public SimpleOTHarmony mergeWith(SimpleOTHarmony o) {
        return new SimpleOTHarmony(sortedDoubles.mergeWith(o.sortedDoubles));
    }
}
