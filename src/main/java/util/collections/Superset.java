/**
 *
 */
package util.collections;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import util.string.CollectionPrinter;

import java.util.*;

/**
 * @author jwvl
 * @date Dec 17, 2014
 */
public class Superset<O extends Object> implements Iterable<Set<O>> {
    private Set<Set<O>> contents;

    public Superset() {
        contents = new LinkedHashSet<Set<O>>();
    }

    public void addSet(Set<O> toAdd) {
        contents.add(toAdd);
    }

    public void addCollection(Collection<O> toAdd) {
        Set<O> asSet = Sets.newHashSet(toAdd);
        contents.add(asSet);
    }

    public int getSize() {
        return contents.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Set<O>> iterator() {
        return contents.iterator();
    }

    public Superset<O> createJoin(Set<O> toAdd) {
        Superset<O> result = this.copy();
        result.addSet(toAdd);
        return result;
    }

    public Superset<O> createAdd(O addedElement, Set<O> addTo) {

        if (contents.contains(addTo)) {
            Superset<O> result = this.copy();
            result.contents.remove(addTo);
            Set<O> grown = Sets.newHashSet(addTo);
            grown.add(addedElement);
            result.contents.add(grown);
            return result;
        } else {
            System.err.println("Error: this set is not in this superset");
            return null;
        }
    }

    /**
     * @return
     */
    private Superset<O> copy() {
        Superset<O> result = new Superset<O>();
        for (Set<O> s : this) {
            result.addCollection(s);
        }
        return result;
    }

    public String toString() {
        StringBuffer result = new StringBuffer("(");
        for (Set<O> s : contents) {
            result.append("(");
            result.append(CollectionPrinter.collectionToString(s, ", "));
            result.append(")");
        }
        result.append(")");
        return result.toString();
    }

    public Collection<List<Set<O>>> getPermutations() {
        return Collections2.permutations(contents);
    }
}
