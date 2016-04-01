/**
 *
 */
package eval.harmony.autosort;

import ranking.OldRankedConstraint;

import java.util.*;

/**
 * @author jwvl
 * @date May 27, 2015
 */
public class LazySortedDoublesArray implements
        Comparable<LazySortedDoublesArray> {
    private final int size;
    private final StratifiedDouble leftmost;
    private final boolean isLazy;
    private SortedDoublesArray sortedArray;
    private List<SortedDoublesArray> unsortedCache;

    private static final Random rnd = new Random();
    public static LazySortedDoublesArray MAX = getMaxValue();
    public static LazySortedDoublesArray MIN = getMinValue();

    private LazySortedDoublesArray(int size, StratifiedDouble leftmost,
                                   boolean isLazy) {
        this.size = size;
        this.leftmost = leftmost;
        this.isLazy = isLazy;
    }

    /**
     * @param lazySortedDoublesArray
     * @param s
     */
    public LazySortedDoublesArray(LazySortedDoublesArray... args) {
        //System.out.println("Merging " + args.length + " arrays.");
        this.isLazy = true;
        int size = 0;
        StratifiedDouble currentLeftmost = StratifiedDouble.HIGHEST_RANKING;
        this.unsortedCache = new ArrayList<SortedDoublesArray>();
        this.sortedArray = SortedDoublesArray.EMPTY;
        for (LazySortedDoublesArray lsa : args) {
            if (currentLeftmost.harmonyCompareTo(lsa.leftmost) > 0) {
                //		System.out.println("Setting leftmost to: " + lsa.leftmost);

                currentLeftmost = lsa.leftmost;
            }
            if (lsa.isFullySorted()) {
                this.unsortedCache.add(lsa.sortedArray);
            } else {
                this.unsortedCache.addAll(lsa.unsortedCache);
            }
            size += lsa.size;
        }
        this.size = size;
        this.leftmost = currentLeftmost;
    }

    /**
     * @param lazySortedDoublesArray
     * @param s
     */
    public LazySortedDoublesArray(SortedDoublesArray values, boolean lazy) {
        this.size = values.size();
        this.leftmost = values.getLeftmostValue();
        //	System.out.println("Generating object of size " + size + ", leftmost:"
        //			+ this.leftmost);
        this.sortedArray = values;
        this.unsortedCache = new ArrayList<SortedDoublesArray>();
        this.isLazy = lazy;
    }

    public static LazySortedDoublesArray createLazyInstance(
            StratifiedDouble[] values) {
        SortedDoublesArray initial = SortedDoublesArray.createInstance(values);
        return new LazySortedDoublesArray(initial, true);
    }

    public static LazySortedDoublesArray createInstance(
            StratifiedDouble[] values) {
        SortedDoublesArray initial = SortedDoublesArray.createInstance(values);
        return new LazySortedDoublesArray(initial, false);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(LazySortedDoublesArray o) {
        if (this == MAX) {
            if (o == MAX) {
                return 0;
            }
            return 1;
        } else if (o == MAX) {
            return -1;
        }
        int result = this.leftmost.harmonyCompareTo(o.leftmost);
        if (result == 0) {
            this.mergeCache();
            o.mergeCache();
            return (sortedArray.compareTo(o.sortedArray));
        }
        return result;
    }

    /**
     * Merge the cached arrays to the SortedArray variable, and clear the cache.
     */
    private void mergeCache() {
        if (!isFullySorted()) {
            this.sortedArray = SortedDoublesArray.merge(unsortedCache);
            unsortedCache.clear();
        }
    }

    public static LazySortedDoublesArray getMin(LazySortedDoublesArray a,
                                                LazySortedDoublesArray b) {
        //System.out.println("Comparing " + a + "\nand       " + b);
        if (a.compareTo(b) < 0)
            return a;
        else
            return b;
    }

    public LazySortedDoublesArray mergeWith(LazySortedDoublesArray s) {
        if (this.isLazy || s.isLazy) {
            return new LazySortedDoublesArray(this, s);
        } else {
            LazySortedDoublesArray result = new LazySortedDoublesArray(
                    this.sortedArray.mergeWith(s.sortedArray), false);
            return result;
        }
    }

    public StratifiedDouble getLeftmostValue() {
        return leftmost;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    private boolean isFullySorted() {
        return unsortedCache.isEmpty();
    }

    public static LazySortedDoublesArray generateRandom(int length, int nStrata) {
        StratifiedDouble[] tempArray = new StratifiedDouble[length];
        for (int i = 0; i < length; i++) {
            double randomValue = rnd.nextDouble() * 100d;
            int randomStratum = rnd.nextInt(nStrata);
            tempArray[i] = StratifiedDouble.of(randomStratum, randomValue);
        }
        Arrays.sort(tempArray);
        return createInstance(tempArray);
    }

    public static LazySortedDoublesArray generateRandomLazy(int length,
                                                            int nStrata) {
        StratifiedDouble[] tempArray = new StratifiedDouble[length];
        for (int i = 0; i < length; i++) {
            double randomValue = rnd.nextDouble() * 100d;
            int randomStratum = rnd.nextInt(nStrata);
            tempArray[i] = StratifiedDouble.of(randomStratum, randomValue);
        }
        Arrays.sort(tempArray);
        return createLazyInstance(tempArray);
    }

    public String toString() {
        if (this == getMaxValue())
            return "[oo]";
        StringBuilder result = new StringBuilder();
        if (!isFullySorted())
            mergeCache();
        result.append(sortedArray.toString());
        return result.toString();
    }

    private static LazySortedDoublesArray getMaxValue() {
        if (MAX == null) {
            MAX = new LazySortedDoublesArray(Integer.MAX_VALUE,
                    StratifiedDouble.HIGHEST_RANKING, false);
        }
        return MAX;
    }

    private static LazySortedDoublesArray getMinValue() {
        if (MIN == null) {
            MIN = new LazySortedDoublesArray(0,
                    StratifiedDouble.LOWEST_RANKING, false);
        }
        return MIN;
    }

    public static LazySortedDoublesArray createFromRankedConstraints(Collection<OldRankedConstraint> constraints) {
        StratifiedDouble[] array = new StratifiedDouble[constraints.size()];
        int counter = 0;
        for (OldRankedConstraint rc : constraints) {
            array[counter++] = StratifiedDouble.of(rc.getStratum(), rc.getDisharmony());
        }
        return createInstance(array);

    }

}
