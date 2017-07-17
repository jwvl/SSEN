package constraints.hierarchy.reimpl;

import constraints.Constraint;
import eval.sample.AbstractSampler;

/**
 * Created by janwillem on 28/06/2017.
 */
public class LazySamplingHierarchy extends Hierarchy {
    private final AbstractSampler sampler;


    protected LazySamplingHierarchy(double[] rankings, int size, Hierarchy parentHierarchy, AbstractSampler sampler) {
        super(rankings, size, parentHierarchy);
        this.sampler = sampler;
    }

    public LazySamplingHierarchy(int expectedSize, Hierarchy parentHierarchy, AbstractSampler sampler) {
        this(new double[expectedSize], expectedSize, parentHierarchy, sampler);
    }

    @Override
    public double getRanking(Constraint c) {
        int constraintId = c.getId();
        if (!isSampled(constraintId)) {
            double parentRanking = getParentHierarchy().getRanking(c);
            double sampled = sampler.sampleDouble(parentRanking);
            rankings[constraintId] = sampled;
        }
        return rankings[constraintId];
    }

    @Override
    public boolean contains(Constraint constraint) {
        return getParentHierarchy().contains(constraint);
    }

    private boolean isSampled(int id) {
        return rankings[id] == 0.0d;
    }
}
