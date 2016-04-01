/**
 *
 */
package util.collections;

import com.google.common.collect.HashMultimap;

import java.util.Set;

/**
 * @author jwvl
 * @date May 26, 2015
 */
public class CountingMultimap<K extends Object, V extends Object> {
    private HashMultimap<K, V> map;
    private FrequencyMap<Pair<K, V>> counts;

    /**
     * @param map
     * @param counts
     */
    public CountingMultimap() {
        this.map = HashMultimap.create();
        this.counts = new FrequencyMap<Pair<K, V>>();
    }

    public void addPair(K key, V value) {
        map.put(key, value);
        counts.addOne(Pair.of(key, value));
    }

    public Set<V> getValueSet(K key) {
        return map.get(key);
    }

    public Set<K> getKeySet() {
        return map.keySet();
    }

    public int getCount(K key, V value) {
        return counts.getCount(Pair.of(key, value));
    }

    public void printValuesForKey(K key) {
        System.out.println("Counts found for " + key);
        for (V v : map.get(key)) {
            Pair<K, V> asPair = Pair.of(key, v);
            System.out.printf("  %s :: %d%n", asPair, counts.getCount(asPair));
        }
    }


}
