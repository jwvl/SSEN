package ranking;

import eval.sample.AbstractSampler;
import ranking.constraints.Constraint;
import ranking.constraints.RankedConstraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by janwillem on 29/03/16.
 */
public class DynamicSampledHierarchy extends Hierarchy {
    private final GrammarHierarchy original;
    private final AbstractSampler sampler;

    public DynamicSampledHierarchy(GrammarHierarchy original, AbstractSampler sampler) {
        this.original = original;
        this.sampler = sampler;
    }

    @Override
    public double getRanking(Constraint c) {
        if (!contains(c)) {
            if (!original.contains(c)) {
                original.addNewConstraint(c);
            }
            double disharmony = original.getRanking(c);
            double rankingValue = sampler.sampleDouble(disharmony);
            addConstraint(c, rankingValue);
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

    public IndexedSampledHierarchy toIndexedSampledHierarchy() {
        List<RankedConstraint> rankedConstraints = new ArrayList<>(original.size());
        for (Constraint c: original) {
            double value = getRanking(c);
            rankedConstraints.add(RankedConstraint.of(c,value));
        }
        Collections.sort(rankedConstraints);
        Collections.reverse(rankedConstraints);
        return new IndexedSampledHierarchy(original, rankedConstraints);
    }

    @Override
    public Iterator<Constraint> iterator() {
        return original.iterator();
    }
}
