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

import java.util.Iterator;

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
        Iterator<Constraint> iter = multiSet.iterator();
        Constraint argMax = iter.next();
        double valMax = con.getRanking(argMax);
        while (iter.hasNext()) {
            Constraint constraint = iter.next();
            double thisVal = con.getRanking(constraint);
            if (thisVal > valMax) {
                valMax = thisVal;
                argMax = constraint;
            }
        }
        if (argMax == null) {
            System.err.println("Argmax is still null??");
            if (multiSet.isEmpty()) {
                System.err.println("Searched set is empty!");
            }
        }
        return argMax;
    }

    public static Multiset<Constraint> getWhereValueIsHigherThan(Multiset<Constraint> multiSet, GrammarHierarchy con, double threshold) {
        Multiset<Constraint> result = HashMultiset.create();
        for (Constraint constraint : multiSet.elementSet()) {
            double thisVal = con.getRanking(constraint);
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
