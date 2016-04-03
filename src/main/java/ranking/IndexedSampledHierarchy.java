package ranking;

import eval.sample.AbstractSampler;
import ranking.constraints.Constraint;
import ranking.constraints.RankedConstraint;

import java.util.Iterator;
import java.util.List;

/**
 * Created by janwillem on 01/04/16.
 */
public class IndexedSampledHierarchy extends SampledHierarchy {
    private final IndexedRanking indexedRanking;
    private final GrammarHierarchy original;

    public IndexedSampledHierarchy(GrammarHierarchy original, List<RankedConstraint> rankedConstraints) {
        super(original);
        this.original = original;
        for (RankedConstraint rankedConstraint: rankedConstraints) {
            put(rankedConstraint.getConstraint(), rankedConstraint.getRanking());
        }
        indexedRanking = new IndexedRanking(rankedConstraints);
    }

    public IndexedSampledHierarchy(GrammarHierarchy original, AbstractSampler sampler) {
        this(original,sampler.sampleHierarchy(original));
    }

    @Override
    public double getRanking(Constraint c) {
        if (!contains(c)) {
            addToOriginalIfNecessary(c);
            return 100.0;
        }
        return getRankingValue(c);
    }

    @Override
    public void addConstraint(Constraint c, double value) {
        put(c, value);
    }

    @Override
    public int size() {
        return original.size();
    }

    @Override
    public List<RankedConstraint> toRankedConstraintList() {
        return original.toRankedConstraintList();
    }

    public int getIndex(Constraint c) {
        if (!contains(c)) {
            addToOriginalIfNecessary(c);
            return c.getId();
        }
        return indexedRanking.getIndex(c);
    }

    public IndexedRanking getIndexedRanking() {
        return indexedRanking;
    }

    @Override
    public Iterator<Constraint> iterator() {
        return original.iterator();
    }
}
