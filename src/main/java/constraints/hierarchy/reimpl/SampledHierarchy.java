package constraints.hierarchy.reimpl;

import constraints.Constraint;
import eval.sample.AbstractSampler;

/**
 * Created by janwillem on 05/08/16.
 */
public class SampledHierarchy extends Hierarchy {
    private final AbstractSampler sampler;
    private final Hierarchy parentHierarchy;

    protected SampledHierarchy(Hierarchy parentHierarchy, double[] contents, AbstractSampler sampler) {
        super(contents,parentHierarchy.getParentCon());
        this.parentHierarchy = parentHierarchy;
        this.sampler = sampler;
    }


    @Override
    public void updateConstraintRanking(Constraint constraint, double delta) {
        System.out.println("Updating ranking of sampled constraints?");
        parentHierarchy.updateConstraintRanking(constraint, delta);
    }

    @Override
    public void addConstraint(Constraint c, double value) {
        parentHierarchy.addConstraint(c,value);
        double sampled = sampler.sampleDouble(value);
        putValue(c, sampled);
    }
}
