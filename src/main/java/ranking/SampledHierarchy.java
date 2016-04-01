package ranking;

import eval.sample.AbstractSampler;
import ranking.constraints.Constraint;

/**
 * Created by janwillem on 29/03/16.
 */
public class SampledHierarchy extends Hierarchy {
    private final GrammarHierarchy original;
    private final AbstractSampler sampler;

    public SampledHierarchy(GrammarHierarchy original, AbstractSampler sampler) {
        super(false);
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
}
