/**
 *
 */
package util.collections;

import java.util.*;

/**
 * @author jwvl
 * @date Nov 16, 2014
 */
public class SetMap<K, V> implements Iterable<K> {
    private Map<K, Set<V>> contents;

    public SetMap() {
        contents = new HashMap<K, Set<V>>();
    }

    public void add(K k, V v) {
        if (!contents.containsKey(k))
            addKey(k);
        Set<V> list = contents.get(k);
        list.add(v);
    }

    public void add(Pair<K, V> pair) {
        add(pair.getLeft(), pair.getRight());
    }

    /**
     * @param k
     */
    public void addKey(K k) {
        Set<V> v = new HashSet<V>();
        contents.put(k, v);

    }

    public void clear(K k) {
        if (!contents.containsKey(k))
            return;
        Set<V> set = contents.get(k);
        set.clear();
    }

    public boolean containsKey(K k) {
        return contents.containsKey(k);
    }

    public Set<V> get(K k) {
        return contents.get(k);
    }

    public Iterator<K> getIterator() {
        return contents.keySet().iterator();
    }

    public Set<K> getKeys() {
        return contents.keySet();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<K> iterator() {
        // TODO Auto-generated method stub
        return getIterator();
    }

    public void remove(K k, V v) {
        if (!contents.containsKey(k))
            return;
        Set<V> set = contents.get(k);
        set.remove(v);
    }

    public void removeKey(K k) {
        contents.remove(k);
    }

    public void removeSet(K k) {
        contents.remove(k);
    }

    public List<Set<V>> toSetList(List<K> keys) {
        List<Set<V>> result = new ArrayList<Set<V>>();
        for (K k : keys) {
            if (contents.containsKey(k))
                result.add(contents.get(k));
        }
        return result;
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        int count = 0;
        for (K k : contents.keySet()) {
            if (count > 0)
                result.append("\n");
            result.append("Key:" + (count++));
            result.append(") " + k.toString());
            int jCount = 0;
            for (V v : contents.get(k)) {
                result.append("\n Value:\t" + (jCount++) + "> " + v.toString());
            }
        }
        return result.toString();
    }

}
