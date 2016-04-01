/**
 *
 */
package util.collections;

import java.util.*;

/**
 * @author jwvl
 * @date Nov 16, 2014
 */
public class ListMap<K, V> implements Iterable<K> {
    private Map<K, List<V>> contents;

    public ListMap() {
        contents = new HashMap<K, List<V>>();
    }

    public void add(Couple<K> couple) {
        add(couple.getLeft(), (V) couple.getRight());
    }

    public void add(K k, V v) {
        if (!contents.containsKey(k))
            addKey(k);
        List<V> list = contents.get(k);
        list.add(v);
    }

    public void add(Pair<K, V> pair) {
        add(pair.getLeft(), pair.getRight());
    }

    public void clear(K k) {
        if (!contents.containsKey(k))
            return;
        List<V> list = contents.get(k);
        list.clear();
    }

    public boolean containsKey(K k) {
//		if (contents.containsKey(k))
//			System.err.println("Yes!  " + k +" is also in ListMap.");
//		else
//			System.out.println("Alas!  " + k +" is not in ListMap. Look:");
//		for (K l: this) {
//			System.out.printf("  %s is equal to %s? %b\n", k.toString(), l.toString(), k.equals(l));
//		}

        return contents.containsKey(k);
    }

    public List<V> get(K k) {
        return contents.get(k);
    }

    public Iterator<K> getIterator() {
        return contents.keySet().iterator();
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<K> iterator() {
        return getIterator();
    }

    public void printAll() {
        System.out.printf("ListMap, size: %d\n", contents.size());
        for (K k : this) {
            System.out.printf("%s has %d entries\n", k.toString(), contents.get(k).size());
        }
    }

    public void remove(K k, V v) {
        if (!contents.containsKey(k))
            return;
        List<V> list = contents.get(k);
        list.remove(v);
    }

    public void removeList(K k) {
        contents.remove(k);
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

    /**
     * @param k
     */
    private void addKey(K k) {
        List<V> v = new ArrayList<V>();
        contents.put(k, v);

    }


}
