/**
 *
 */
package learn.update;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import learn.ViolatedCandidate;
import ranking.GrammarHierarchy;
import ranking.constraints.Constraint;

/**
 * @author jwvl
 * @date 19/09/2015
 */
public class UpdateUtils {

    public static Multiset<Constraint> subtract(Multiset<Constraint> minuend,
                                                Multiset<Constraint> subtrahend) {
        return Multisets.difference(minuend, subtrahend);
    }

    public static Multiset<Constraint> getViolatedByLearner(ViolatedCandidate learner, ViolatedCandidate target) {
        return Multisets.difference(learner.getConstraints(), target.getConstraints());
    }

    public static Multiset<Constraint> getViolatedByTarget(ViolatedCandidate learner, ViolatedCandidate target) {
        return Multisets.difference(target.getConstraints(), learner.getConstraints());
    }

    public static Constraint getMax(Multiset<Constraint> multiSet, GrammarHierarchy con) {
        double valMax = Double.MIN_VALUE;
        Constraint argMax = null;
        for (Constraint constraint : multiSet.elementSet()) {
            double thisVal = con.getStratifiedRanking(constraint).getValue();
            if (thisVal > valMax) {
                valMax = thisVal;
                argMax = constraint;
            }
        }
        if (argMax == null) {
            System.err.println("Argmax is still null??");
        }
        return argMax;
    }

    public static Multiset<Constraint> getWhereValueIsHigherThan(Multiset<Constraint> multiSet, GrammarHierarchy con, double threshold) {
        Multiset<Constraint> result = HashMultiset.create();
        for (Constraint constraint : multiSet.elementSet()) {
            double thisVal = con.getStratifiedRanking(constraint).getValue();
            if (thisVal > threshold) {
                result.add(constraint, multiSet.count(constraint));
            }
        }
        return result;
    }

    public static void multisetToUpdateAction(Multiset<Constraint> multiset, double delta, UpdateAction updateAction) {
        double weightedDelta = delta / multiset.size();
        for (Constraint constraint : multiset) {
            updateAction.add(constraint, weightedDelta);
        }
    }

}
