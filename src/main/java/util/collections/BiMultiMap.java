/**
 *
 */
package util.collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;

/**
 * Bidirectional implementation of Guava's Multimap
 *
 * @author jwvl
 * @date May 24, 2015
 */
public class BiMultiMap<O, P> {
    Multimap<O, P> rightward;
    Multimap<P, O> leftward;

    public BiMultiMap() {
        rightward = HashMultimap.create();
        leftward = HashMultimap.create();
    }

    public void addElements(O o, P p) {
        rightward.put(o, p);
        leftward.put(p, o);
    }

    public void unlink(O o, P p) {
        rightward.get(o).remove(p);
        leftward.get(p).remove(o);
    }

    /**
     * Warning: this may be a very expensive operation!
     *
     * @param o
     */
    public void removeLeft(O o) {
        rightward.removeAll(o);
        for (P p : leftward.keySet()) {
            leftward.get(p).remove(o);
        }
    }

    /**
     * Warning: this may be a very expensive operation!
     *
     * @param o
     */
    public void removeRight(P p) {
        leftward.removeAll(p);
        for (O o : rightward.keySet()) {
            rightward.get(o).remove(p);
        }
    }

    public boolean containsLink(O o, P p) {
        if (rightward.size() > leftward.size()) {
            Collection<P> col = rightward.get(o);
            return col.contains(p);
        } else {
            Collection<O> col = leftward.get(p);
            return col.contains(o);

        }
    }

}
