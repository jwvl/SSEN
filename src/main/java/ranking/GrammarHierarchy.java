/**
 *
 */
package ranking;

import eval.harmony.autosort.StratifiedDouble;
import ranking.constraints.Constraint;
import ranking.constraints.RankedConstraint;
import ranking.violations.ConstraintViolation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author jwvl
 * @date Aug 10, 2015
 * An "implementation" of CON. Translates a Constraint object to a RankedConstraint,
 * and adds new RankedConstraints if necessary.
 */
public class GrammarHierarchy extends Hierarchy {
    private static double DEFAULT_INITIAL_DISHARMONY = 100.0;
    private List<Constraint> constraintList;

    public GrammarHierarchy() {
        super();
        constraintList = new ArrayList<>();
        constraintList = Collections.synchronizedList(constraintList);
    }

    @Override
    public int size() {
        return constraintList.size();
    }

    @Override
    public List<RankedConstraint> toRankedConstraintList() {
            List<RankedConstraint> rankedList = new ArrayList<>(size());
            for (Constraint constraint : this) {
                rankedList.add(RankedConstraint.of(constraint, getRankingValue(constraint)));
            }
            Collections.sort(rankedList);
            Collections.reverse(rankedList);
            return rankedList;
    }



    @Override
    public double getRanking(Constraint constraint) {

        if (!contains(constraint)) {
            addNewConstraint(constraint);
        }
        return getRankingValue(constraint);
    }

    public void addNewConstraint(Constraint constraint) {
        double initValue = DEFAULT_INITIAL_DISHARMONY + constraint.getInitialBias();
        addConstraint(constraint,initValue);
        constraintList.add(constraint);
        System.out.println("Adding constraint " + constraint +" to grammar!");
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
        GrammarHierarchy result = new GrammarHierarchy();
        for (Constraint constraint : this) {
            double value = getRanking(constraint);
            result.addConstraint(constraint, value);
        }
        return result;
    }


    @Override
    public Iterator<Constraint> iterator() {
        return constraintList.iterator();
    }
}
