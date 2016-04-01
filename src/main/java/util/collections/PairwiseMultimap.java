/**
 *
 */
package util.collections;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import forms.morphosyntax.AttributeSet;
import forms.morphosyntax.SyntacticCategory;

import java.util.Collection;
import java.util.Set;

/**
 * @author jwvl
 * @date Aug 1, 2015
 */
public class PairwiseMultimap<L extends Object, R extends Object> {
    private Multimap<L, R> multimap;
    private boolean isPairCacheUpToDate;
    private Set<Pair<L, R>> pairs;

    private PairwiseMultimap() {
        multimap = HashMultimap.create();
        isPairCacheUpToDate = false;
    }

    public static <L, R> PairwiseMultimap<L, R> create() {
        return new PairwiseMultimap<L, R>();
    }

    public void addPair(Pair<L, R> pair) {
        multimap.put(pair.getLeft(), pair.getRight());
        isPairCacheUpToDate = false;
    }

    private void updatePairCache() {
        pairs = Sets.newHashSet();
        for (L l : multimap.keySet()) {
            for (R r : multimap.get(l)) {
                pairs.add(Pair.of(l, r));
            }
        }
        isPairCacheUpToDate = true;
    }

    public Set<Pair<L, R>> getPairSet() {
        if (!isPairCacheUpToDate)
            updatePairCache();
        return pairs;
    }

    /**
     * @param left  Left member of pair
     * @param right Right member of pair
     */
    public void addPairMembers(L left, R right) {
        this.addPair(Pair.of(left, right));
    }

    /**
     * @param pairs2
     */
    public void addPairs(
            Collection<Pair<L, R>> pairs) {
        for (Pair<L, R> pair : pairs) {
            this.addPair(pair);
        }
    }

    /**
     * @param pair
     * @return
     */
    public boolean hasPair(Pair<SyntacticCategory, AttributeSet> pair) {
        return multimap.containsEntry(pair.getLeft(), pair.getRight());
    }


}
