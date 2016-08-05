package eval.sample;

import constraints.hierarchy.reimpl.Hierarchy;
import eval.harmony.autosort.StratifiedDouble;
import constraints.Constraint;
import constraints.RankedConstraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Sampler object creates a stochastic constraints.ranking from a Hierarchy. In standard
 * GLA this is implemented with a Gaussian random number generator,
 * but other variants are thinkable as well (e.g. uniform random numbers)
 *
 * @author jwvl
 * @date Dec 5, 2014
 */
public abstract class AbstractSampler {
    protected long counter = 0;
    private boolean lazy = true;

    public void incrementSampleCount() {
        counter++;
    }

    public abstract double sampleDouble();


    public boolean isLazy() {
        return lazy;
    }

    public void setLazy(boolean lazy) {
        this.lazy = lazy;
    }

    /**
     * @return
     */
    public StratifiedDouble sampleStratifiedDouble(StratifiedDouble old) {
        double toAdd = sampleDouble();
        //	System.out.printf("Sampled %.3f%n",toAdd);
        return StratifiedDouble.of(old.getStratum(), old.getValue() + toAdd);
    }

    public double sampleDouble(double old) {
        return old + sampleDouble();
    }

    public List<RankedConstraint> sampleHierarchy(Hierarchy hierarchy) {
        List<RankedConstraint> result = new ArrayList<>(hierarchy.size());
        for (Constraint constraint: hierarchy) {
            double oldValue = hierarchy.getRanking(constraint);
            RankedConstraint toPut = RankedConstraint.of(constraint, sampleDouble(oldValue));
            result.add(toPut);
        }
        Collections.sort(result);
        Collections.reverse(result);
        return result;
    }

}
