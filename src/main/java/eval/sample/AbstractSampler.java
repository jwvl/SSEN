package eval.sample;

import eval.Evaluation;
import eval.harmony.autosort.StratifiedDouble;
import ranking.GrammarHierarchy;
import ranking.OldRankedConstraint;
import ranking.constraints.Constraint;
import ranking.constraints.RankedConstraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Sampler object creates a stochastic ranking from a Hierarchy. In standard
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

    public void sampleConstraints(Iterable<OldRankedConstraint> rcs, Evaluation e) {
        for (OldRankedConstraint rc : rcs) {
            sampleConstraint(rc, e);
        }
    }

    public void sampleConstraint(OldRankedConstraint rc, Evaluation e) {
        rc.conflateWithNoise(sampleDouble(), e.getId(), lazy);
    }

    public void sampleConstraints(Iterable<OldRankedConstraint> rcs) {
        for (OldRankedConstraint rc : rcs) {
            rc.conflateWithNoise(sampleDouble(), 0L, false);
        }
    }

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

    public List<RankedConstraint> sampleHierarchy(GrammarHierarchy hierarchy) {
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
