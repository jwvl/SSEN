/**
 *
 */
package ranking;

import ranking.constraints.Constraint;

/**
 * An extension of a RankedConstraint whose update rate is subject to some
 * 'plasticity'.
 *
 * @author jwvl
 * @date Dec 5, 2014
 */
public class PlasticOldRankedConstraint extends OldRankedConstraint {
    protected double plasticity;
    protected boolean decaysFromLearning;
    protected double decayRate;

    private PlasticOldRankedConstraint(Constraint c) {
        super(c);
    }

    private PlasticOldRankedConstraint(OldRankedConstraint rc) {
        this(rc.getConstraint());

    }

    public PlasticOldRankedConstraint createInstance(Constraint c,
                                                     double initPlasticity) {
        PlasticOldRankedConstraint result = new PlasticOldRankedConstraint(c);
        result.plasticity = initPlasticity;
        result.decaysFromLearning = false;
        decayRate = 0;
        return result;
    }

    public PlasticOldRankedConstraint createDecayingInstance(Constraint c,
                                                             double initPlasticity, double decay) {
        PlasticOldRankedConstraint result = new PlasticOldRankedConstraint(c);
        result.plasticity = initPlasticity;
        result.decaysFromLearning = true;
        return result;
    }

    @Override
    public OldRankedConstraint copy() {
        PlasticOldRankedConstraint result = new PlasticOldRankedConstraint(
                this.getConstraint());
        result.sortingValues = this.sortingValues.copy();
        result.plasticity = this.plasticity;
        result.decaysFromLearning = this.decaysFromLearning;
        result.decayRate = this.decayRate;
        return result;
    }

    @Override
    public void updateRankingValue(double delta) {
        super.updateRankingValue(delta);
        if (decaysFromLearning) {
            decayPlasticity(decayRate);
        }
    }

    public void decayPlasticity(double rate) {
        plasticity *= rate;
    }

    public double getPlasticity() {
        return plasticity;
    }

    public boolean isDecaysFromLearning() {
        return decaysFromLearning;
    }

    public double getDecayRate() {
        return decayRate;
    }

    public void setPlasticity(double plasticity) {
        this.plasticity = plasticity;
    }

    public void setDecaysFromLearning(boolean decaysFromLearning) {
        this.decaysFromLearning = decaysFromLearning;
    }

    public void setDecayRate(double decayRate) {
        this.decayRate = decayRate;
    }

}
