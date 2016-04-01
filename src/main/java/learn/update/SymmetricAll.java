/**
 *
 */
package learn.update;

import com.google.common.collect.Multiset;
import learn.ViolatedCandidate;
import ranking.GrammarHierarchy;
import ranking.constraints.Constraint;

import java.util.Collection;

/**
 * @author jwvl
 * @date Aug 22, 2015
 */
public class SymmetricAll implements UpdateAlgorithm {


    public void update(GrammarHierarchy con, Collection<ViolatedCandidate> lCandidates,
                       Collection<ViolatedCandidate> tCandidates, double delta) {
        for (ViolatedCandidate lCandidate : lCandidates) {
            Multiset<Constraint> constraintMultiset = lCandidate.getConstraints();

            for (Constraint rc : constraintMultiset.elementSet()) {
                double multipliedDelta = constraintMultiset.count(rc) * delta;
                con.updateConstraintRanking(rc, multipliedDelta);
                System.out.println("Updating " + rc + " by " + multipliedDelta);
            }
        }

        for (ViolatedCandidate tCandidate : tCandidates) {
            Multiset<Constraint> constraintMultiset = tCandidate.getConstraints();
            for (Constraint rc : constraintMultiset.elementSet()) {
                double multipliedDelta = constraintMultiset.count(rc) * delta;
                con.updateConstraintRanking(rc, -multipliedDelta);
                System.out.println("Updating " + rc + " by " + multipliedDelta);
            }
        }
    }


    public UpdateAction getUpdate(GrammarHierarchy con, ViolatedCandidate lCandidate,
                                  ViolatedCandidate tCandidate, double delta) {
        UpdateAction updateAction = UpdateAction.create();

        Multiset<Constraint> lConstraintMultiset = lCandidate.getConstraints();
        for (Constraint rc : lConstraintMultiset.elementSet()) {
            updateAction.add(rc, delta);
        }

        Multiset<Constraint> tConstraintMultiset = tCandidate.getConstraints();
        for (Constraint rc : tConstraintMultiset.elementSet()) {
            updateAction.add(rc, -delta);
        }

        return updateAction;
    }

}
