/**
 *
 */
package ranking;

import eval.harmony.autosort.StratifiedDouble;
import ranking.constraints.Constraint;
import ranking.violations.ConstraintViolation;

/**
 * @author jwvl
 * @date Aug 10, 2015
 * An "implementation" of CON. Translates a Constraint object to a RankedConstraint,
 * and adds new RankedConstraints if necessary.
 */
public class GrammarHierarchy extends Hierarchy {
    private static double DEFAULT_INITIAL_DISHARMONY = 100.0;

    public GrammarHierarchy(boolean concurrent) {
        super(concurrent);
    }


    public void addNewConstraint(Constraint constraint) {
        double initValue = DEFAULT_INITIAL_DISHARMONY + constraint.getInitialBias();
        addConstraint(constraint, initValue);
    }

    @Override
    public double getRanking(Constraint constraint) {

        if (!contains(constraint)) {
            addNewConstraint(constraint);
        }
        return getRankingValue(constraint);
    }

    public void addConstraint(Constraint constraint, double disharmony) {
        put(constraint, disharmony);
    }


    public StratifiedDouble getStratifiedRanking(Constraint constraint) {
        double value = getRanking(constraint);
        return StratifiedDouble.of(constraint.getStratum(), value);
    }


    /**
     * @param constraint
     * @param delta
     */
    public void updateConstraintRanking(Constraint constraint, double delta) {
        if (!contains(constraint)) {
            System.err.println("Should not happen..! Could not find constraint " + constraint);
        }
        double newValue = getRanking(constraint) + delta;
        put(constraint, newValue);
    }

    /**
     * @param c
     * @return
     */
    public ConstraintViolation toConstraintViolation(Constraint c) {
        return ConstraintViolation.of(c, getStratifiedRanking(c));
    }


    /**
     * @return
     */
    public GrammarHierarchy deepCopy() {
        GrammarHierarchy result = new GrammarHierarchy(this.isConcurrent());
        for (Constraint constraint : this) {
            double value = getRanking(constraint);
            result.addConstraint(constraint, value);
        }
        return result;
    }


}
