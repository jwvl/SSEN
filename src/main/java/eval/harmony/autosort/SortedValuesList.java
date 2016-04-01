/**
 *
 */
package eval.harmony.autosort;

import com.google.common.collect.Ordering;
import ranking.SortingValues;
import util.collections.AutoSortedList;

import java.util.*;

/**
 * An implementation of auto-sorted ranked violations
 * using an ArrayList.
 *
 * @author jwvl
 * @date Dec 4, 2014
 */
public class SortedValuesList extends AutoSortedList<SortingValues> {

    private List<SortingValues> contents;
    private boolean sorted;

    public static SortedValuesList createEmpty() {
        return new SortedValuesList();
    }

    /**
     * @param toAdd
     */
    public SortedValuesList(List<SortingValues> toAdd) {
        contents = new ArrayList<SortingValues>(toAdd.size());
        if (!toAdd.isEmpty()) {
            SortingValues currMax = toAdd.get(0);
            for (int i = 0; i < toAdd.size(); i++) {
                SortingValues iSv = toAdd.get(i);
                currMax = elementOrdering.max(currMax, iSv);
                contents.add(iSv);
            }
            sorted = false;
            setCachedMax(currMax);
        } else {
            sorted = true;
            clearCachedMax();
        }
    }

    private SortedValuesList() {
        contents = new ArrayList<SortingValues>();
        sorted = true;
        clearCachedMax();
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#insert(util.collections.Orderable)
     */
    @Override
    public void add(SortingValues o) {
        clearCachedMax();
        contents.add(o);
        sorted = false;
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#insertAll(java.util.Collection)
     */
    @Override
    public void addAll(Collection<SortingValues> toAdd) {
        clearCachedMax();
        contents.addAll(toAdd);
        sorted = false;
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#copy()
     */
    @Override
    public AutoSortedList<SortingValues> copy() {
        SortedValuesList result = new SortedValuesList(this.contents);
        result.sorted = this.sorted;
        result.cachedMax = this.cachedMax;
        return result;
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#getContentsAsList()
     */
    @Override
    public SortingValues[] getContentsAsArray() {
        return contents.toArray(new SortingValues[size()]);
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#getElementOrdering()
     */
    @Override
    public Ordering<SortingValues> getElementOrdering() {
        return SortingValues.getOrdStatic();
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#getMax()
     */
    @Override
    public SortingValues getMax() {
        if (!isEmpty() && maxCached == false) {
            lazySort();
            setCachedMax(contents.get(0));
        }
        return cachedMax;
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#getMin()
     */
    @Override
    public SortingValues getMin() {
        lazySort();
        return contents.get(size() - 1);
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<SortingValues> iterator() {
        lazySort();
        return contents.iterator();
    }

    /**
     * Sorts the contents iff they aren't sorted already
     */
    public void lazySort() {
        if (!sorted) {
            sort();
        }

    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#mergeWith(util.collections.AutoSortedList)
     */
    @Override
    public AutoSortedList<SortingValues> mergeWith(
            AutoSortedList<SortingValues> other) {
        AutoSortedList<SortingValues> result = this.copy();
        for (SortingValues sv : other) {
            result.add(sv);
        }
        return result;
    }

    @Override
    public int size() {
        return contents.size();
    }

    public void sort() {
        Collections.sort(contents, elementOrdering.reverse());
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
            SortingValues sv = contents.get(i);
            result.append(String.format("%d:%.2f", sv.getStratum(),
                    sv.getDisharmony()));
            if (i < size() - 1)
                result.append(", ");
        }
        result.append(")");
        return result.toString();
    }

}
