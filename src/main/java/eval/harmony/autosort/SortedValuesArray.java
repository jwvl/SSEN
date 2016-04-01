/**
 *
 */
package eval.harmony.autosort;

import com.google.common.collect.Ordering;
import org.apache.commons.collections4.iterators.ArrayIterator;
import ranking.SortingValues;
import util.collections.AutoSortedList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author jwvl
 * @date Dec 4, 2014
 */
public class SortedValuesArray extends AutoSortedList<SortingValues> {
    private SortingValues[] contents;

    public static SortedValuesArray createEmpty() {
        return new SortedValuesArray(0);
    }

    /**
     * @param orig
     */
    public SortedValuesArray(List<SortingValues> orig) {
        contents = orig.toArray(new SortingValues[orig.size()]);
        setCachedMax(getElementOrdering().max(orig));
        sorted = false;
    }

    private SortedValuesArray(int size) {
        contents = new SortingValues[size];
        clearCachedMax();
    }

    /*
     * (non-Javadoc)
     *
     * @see util.collections.AutoSortedList#insert(util.collections.Orderable)
     */
    @Override
    public void add(SortingValues o) {
        SortingValues[] newContents = new SortingValues[size() + 1];
        for (int i = 0; i < size(); i++) {
            newContents[i] = contents[i];
        }
        newContents[size()] = o;
        contents = newContents;
        clearCachedMax();
        sorted = false;
    }

    /*
     * (non-Javadoc)
     *
     * @see util.collections.AutoSortedList#insertAll(java.util.Collection)
     */
    @Override
    public void addAll(Collection<SortingValues> toAdd) {
        SortingValues[] newContents = new SortingValues[size() + toAdd.size()];
        for (int i = 0; i < size(); i++) {
            newContents[i] = contents[i];
        }
        int count = 0;
        for (SortingValues sv : toAdd) {
            newContents[size() + count] = sv;
        }
        cachedMax = null;
        sorted = false;

    }

    /*
     * (non-Javadoc)
     *
     * @see util.collections.AutoSortedList#copy()
     */
    @Override
    public AutoSortedList<SortingValues> copy() {
        SortedValuesArray result = new SortedValuesArray(this.size());
        result.contents = this.contents.clone();
        result.sorted = this.sorted;
        result.cachedMax = this.cachedMax;
        result.maxCached = this.maxCached;
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see util.collections.AutoSortedList#getContentsAsList()
     */
    @Override
    public SortingValues[] getContentsAsArray() {
        return contents;
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#getElementOrdering()
     */
    @Override
    public Ordering<SortingValues> getElementOrdering() {
        return SortingValues.getOrdStatic();
    }

    /*
     * (non-Javadoc)
     *
     * @see util.collections.AutoSortedList#getMax()
     */
    @Override
    public SortingValues getMax() {
        if (cachedMax == null && !isEmpty()) {
            sort();
            cachedMax = contents[0];
        }
        return cachedMax;
    }

    /*
     * (non-Javadoc)
     *
     * @see util.collections.AutoSortedList#getMin()
     */
    @Override
    public SortingValues getMin() {
        lazySort();
        return contents[size() - 1];
    }

    public Iterator<SortingValues> iterator() {
        lazySort();
        return new ArrayIterator<SortingValues>(contents);
    }

    /**
     * Sorts the contents iff they aren't sorted already
     */
    public void lazySort() {
        if (!sorted) {
            sort();
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * util.collections.AutoSortedList#mergeWith(util.collections.AutoSortedList
     * )
     */
    @Override
    public AutoSortedList<SortingValues> mergeWith(
            AutoSortedList<SortingValues> other) {
        SortedValuesArray result = new SortedValuesArray(this.size()
                + other.size());
        for (int i = 0; i < size(); i++) {
            result.contents[i] = contents[i];
        }
        SortedValuesArray o = (SortedValuesArray) other;
        for (int i = 0; i < other.size(); i++) {
            result.contents[size() + i] = o.contents[i];
        }
        return result;
    }

    @Override
    public int size() {
        return contents.length;
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#sort()
     */
    public void sort() {
        Arrays.sort(contents, elementOrdering.reverse());
        sorted = true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        lazySort();
        StringBuffer result = new StringBuffer("(");
        for (int i = 0; i < size(); i++) {
            SortingValues sv = contents[i];
            result.append(String.format("%d:%.2f", sv.getStratum(),
                    sv.getDisharmony()));
            if (i < size() - 1)
                result.append(", ");
        }
        result.append(")");
        return result.toString();
    }


}
