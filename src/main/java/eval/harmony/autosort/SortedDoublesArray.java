/**
 *
 */
package eval.harmony.autosort;

import ranking.OldRankedConstraint;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date May 27, 2015
 */
public class SortedDoublesArray implements Comparable<SortedDoublesArray> {
    private final StratifiedDouble[] values;

    public static SortedDoublesArray EMPTY = new SortedDoublesArray(
            new StratifiedDouble[0]);

    private SortedDoublesArray(StratifiedDouble[] values) {
        Arrays.sort(values);
        this.values = values;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SortedDoublesArray arg0) {
        int result = 0;
        for (int i = 0; i < size() && i < arg0.size(); i++) {
            result = (values[i].harmonyCompareTo(arg0.values[i]));

            //System.out.println(values[i] + " < " + arg0.values[i] + " ? " + result);
            if (result != 0)
                return result;
        }
        return size() - arg0.size();
    }

    public static SortedDoublesArray createInstance(StratifiedDouble[] values) {
        SortedDoublesArray result = new SortedDoublesArray(values);
        return result;

    }

    public SortedDoublesArray mergeWith(SortedDoublesArray s) {
        StratifiedDouble[] tempArray = new StratifiedDouble[size() + s.size()];
        int i = 0, j = 0, k = 0;
        while (i < size() && j < s.size()) {
            if (values[i].compareTo(s.values[j]) < 0) {
                tempArray[k++] = values[i++];

            } else {
                tempArray[k++] = s.values[j++];
            }

        }

        while (i < size()) {
            tempArray[k++] = values[i++];
        }

        while (j < s.size()) {
            tempArray[k++] = s.values[j++];

        }
        return new SortedDoublesArray(tempArray);

    }

    public static SortedDoublesArray merge(List<SortedDoublesArray> toMerge) {
        int numElements = toMerge.size();
        if (numElements == 1) {
            return toMerge.get(0);
        } else if (numElements == 2) {
            return toMerge.get(0).mergeWith(toMerge.get(1));
        }
        int resultSize = 0;
        for (SortedDoublesArray sda : toMerge) {
            resultSize += sda.size();
        }
        StratifiedDouble[] tempArray = new StratifiedDouble[resultSize];
        int start = 0;
        for (SortedDoublesArray sda : toMerge) {

            System.arraycopy(sda.values, 0, tempArray, start, sda.size());
            start += sda.size();

        }
        Arrays.sort(tempArray);
        return new SortedDoublesArray(tempArray);
    }

    public StratifiedDouble getLeftmostValue() {
        return isEmpty() ? StratifiedDouble.LOWEST_RANKING : values[0];
    }

    public int size() {
        return values.length;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(Arrays.toString(values));
        return result.toString();
    }

    public static SortedDoublesArray createFromRankedConstraints(Collection<OldRankedConstraint> constraints) {
        StratifiedDouble[] array = new StratifiedDouble[constraints.size()];
        int counter = 0;
        for (OldRankedConstraint rc : constraints) {
            array[counter++] = StratifiedDouble.of(rc.getStratum(), rc.getDisharmony());
        }
        return createInstance(array);

    }

    public static SortedDoublesArray createFromDisharmonies(Collection<StratifiedDouble> constraints) {
        StratifiedDouble[] array = new StratifiedDouble[constraints.size()];
        return createInstance(constraints.toArray(array));

    }

}
