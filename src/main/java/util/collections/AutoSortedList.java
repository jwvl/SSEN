/**
 *
 */
package util.collections;

import com.google.common.collect.Ordering;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author jwvl
 * @date Dec 3, 2014
 */
public abstract class AutoSortedList<O extends Orderable<O>> implements Iterable<O>, Orderable<AutoSortedList<O>> {

    /**
     * @return Number of elements in list.
     */
    protected O cachedMax;
    protected final Ordering<O> elementOrdering = getElementOrdering();
    protected boolean maxCached;
    protected boolean sorted;

    public AutoSortedList<O> abstractMergeWith(AutoSortedList<O> other) {
        O maxToSet = getMaxCachedMax(other);
        AutoSortedList<O> result = mergeWith(other);
        result.sorted = false;
        if (maxToSet != null) {
            result.cachedMax = maxToSet;
            result.maxCached = true;
        } else {
            result.clearCachedMax();
        }
        return result;
    }

    public abstract void add(O o);

    public abstract void addAll(Collection<O> toAdd);

    public abstract AutoSortedList<O> copy();

    public abstract O[] getContentsAsArray();

    public abstract Ordering<O> getElementOrdering();

    public String getListedContents(String header) {
        StringBuffer result = new StringBuffer(header);
        int count = 0;
        for (O o : this) {
            result.append(String.format("\n%d: ", count++));
            result.append(o);
        }
        return result.toString();
    }

    public abstract O getMax();

    public O getMaxCachedMax(AutoSortedList<O> other) {
        // If either is not cached, we cannot determine max
        if (!this.maxCached || !other.maxCached)
            return null;
        return elementOrdering.max(cachedMax, other.cachedMax);
    }

    public abstract O getMin();

    public Ordering<AutoSortedList<O>> getOrdering() {
        return new Ordering<AutoSortedList<O>>() {

            @Override
            public int compare(AutoSortedList<O> arg0, AutoSortedList<O> arg1) {
                //First compare maxes
                int result = elementOrdering.compare(arg0.getMax(), arg1.getMax());
                if (result != 0)
                    return result;
                Iterator<O> it0 = arg0.iterator();
                Iterator<O> it1 = arg1.iterator();
                it0.next();
                it1.next();
                while (result == 0 && it0.hasNext() && it1.hasNext()) {
                    result = elementOrdering.compare(it0.next(), it1.next());

                }
                return arg0.size() - arg1.size();
            }
        };
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public abstract void lazySort();

    public void printFirstN(String header, int n) {
        Iterator<O> itr = iterator();
        int count = 0;
        while (count < n && itr.hasNext()) {
            System.out.print(itr.next() + ", ");
            count++;
        }
        System.out.println();
    }

    //public abstract void setLazyMax(O o);
    public abstract int size();

    protected void clearCachedMax() {
        maxCached = false;
        cachedMax = null;
    }

    protected O getCachedMax() {
        return cachedMax;
    }

    protected abstract AutoSortedList<O> mergeWith(AutoSortedList<O> other);

    protected void setCachedMax(O o) {
        if (o == null) {
            throw new NullPointerException("Trying to set cached max to null. Use clearCachedMax instead.");
        }
        maxCached = true;
        cachedMax = o;
    }

}
