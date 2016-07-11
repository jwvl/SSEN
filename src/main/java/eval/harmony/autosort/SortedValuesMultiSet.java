/**
 *
 */
package eval.harmony.autosort;

import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultiset;
import ranking.SortingValues;
import util.collections.AutoSortedList;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author jwvl
 * @date Dec 4, 2014
 */
public class SortedValuesMultiSet extends AutoSortedList<SortingValues> {

    TreeMultiset<SortingValues> contents;
    int size;

    /**
     * @param input
     */
    public static SortedValuesMultiSet fromValues(List<SortingValues> input) {
        SortedValuesMultiSet result = new SortedValuesMultiSet();
        result.contents.addAll(input);
        result.size = input.size();
        return result;
    }

    private SortedValuesMultiSet() {
        contents = TreeMultiset.create(elementOrdering.reverse());
        size = 0;
    }

    private SortedValuesMultiSet(SortedValuesMultiSet other) {
        this();
        contents.addAll(other.contents);
        size = other.size;
        sorted = true;
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#add(util.collections.Orderable)
     */
    @Override
    public void add(SortingValues o) {
        contents.add(o);
        size++;
        sorted = true;
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#insertAll(java.util.Collection)
     */
    @Override
    public void addAll(Collection<SortingValues> toAdd) {
        contents.addAll(toAdd);
        size += toAdd.size();
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#copy()
     */
    @Override
    public AutoSortedList<SortingValues> copy() {
        return new SortedValuesMultiSet(this);
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
        return contents.firstEntry().getElement();
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#getMin()
     */
    @Override
    public SortingValues getMin() {
        return contents.lastEntry().getElement();
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<SortingValues> iterator() {
        return contents.iterator();
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#lazySort()
     */
    @Override
    public void lazySort() {
        // Does nothing, is already sorted!
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#mergeWith(util.collections.AutoSortedList)
     */
    @Override
    public AutoSortedList<SortingValues> mergeWith(
            AutoSortedList<SortingValues> other) {
        SortedValuesMultiSet result = new SortedValuesMultiSet(this);
//		SortedValuesMultiSet o = (SortedValuesMultiSet) other;
//		result.contents.addAll(o.contents);
        for (SortingValues sv : other) {
            result.contents.add(sv);
        }
        result.size = this.size + other.size();
        result.cachedMax = result.getMax();
        return result;
    }

    /* (non-Javadoc)
     * @see util.collections.AutoSortedList#getNumSteps()
     */
    @Override
    public int size() {
        return size;
    }


}
