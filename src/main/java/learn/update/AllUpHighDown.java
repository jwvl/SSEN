/**
 *
 */
package learn.update;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import constraints.Constraint;
import constraints.hierarchy.reimpl.Hierarchy;
import learn.ViolatedCandidate;

import java.util.Collection;

/**
 * @author jwvl
 * @date 19/09/2015
 */
public class AllUpHighDown implements UpdateAlgorithm {


    public void update(Hierarchy con, Collection<ViolatedCandidate> lCandidates,
                       Collection<ViolatedCandidate> tCandidates, double delta) {
        if (lCandidates.size() == 1 && tCandidates.size() == 1) {
            updateSingles(con, lCandidates.iterator().next(), tCandidates.iterator().next(), delta);
        } else {
            // TODO ever implement this?
            System.err.println("Error: updating on candidate sets not implemented yet for this method.");
            System.exit(0);
        }
    }

    private void updateSingles(Hierarchy con, ViolatedCandidate lCandidate, ViolatedCandidate tCandidate,
                               double delta) {
        // TODO stratify this strategy!
        Multiset<Constraint> targetPreferringView = UpdateUtils.getViolatedByLearner(lCandidate, tCandidate);
        Multiset<Constraint> learnerPreferringView = UpdateUtils.getViolatedByTarget(lCandidate, tCandidate);
        Multiset<Constraint> learnerPreferringHigh = HashMultiset.create(learnerPreferringView.size());
        double promoteWeightedDelta = delta / targetPreferringView.size();
        double maxTPreferringRanking = Double.MIN_VALUE;
        for (Constraint constraint : targetPreferringView.elementSet()) {
            maxTPreferringRanking = Math.max(maxTPreferringRanking, con.getRanking(constraint));
            double multipliedDelta = (targetPreferringView.count(constraint) * promoteWeightedDelta);
            con.changeConstraintRanking(constraint, multipliedDelta);
        }

        for (Constraint constraint : learnerPreferringView.elementSet()) {
            double thisRanking = con.getRanking(constraint);
            if (thisRanking > maxTPreferringRanking) {
                int count = learnerPreferringView.count(constraint);
                learnerPreferringHigh.add(constraint, count);
            }
        }
        // If 'higher' set is empty, just add argMax to it
        if (learnerPreferringHigh.isEmpty()) {
            Constraint argMax = UpdateUtils.getMax(learnerPreferringView, con);
            learnerPreferringHigh.add(argMax);
        }

        Multiset<Constraint> toIterateOver = learnerPreferringHigh;

        double demoteWeightedDelta = -(delta / toIterateOver.size());
        for (Constraint constraint : toIterateOver.elementSet()) {
            double multipliedDelta = toIterateOver.count(constraint) * demoteWeightedDelta;
            con.changeConstraintRanking(constraint, multipliedDelta);
            // System.out.println("↓ Updating " + constraint +" by " +
            // multipliedDelta);
        }
    }


    public UpdateAction getUpdate(Hierarchy con, ViolatedCandidate lCandidate,
                                  ViolatedCandidate tCandidate, double delta) {
        UpdateAction result = UpdateAction.create();
        Multiset<Constraint> violatedByLearner = UpdateUtils
                .getViolatedByLearner(lCandidate, tCandidate);
        Multiset<Constraint> violatedByTarget = UpdateUtils.getViolatedByTarget(lCandidate, tCandidate);
        Multiset<Constraint> toPromote = violatedByLearner.isEmpty() ? lCandidate.getConstraints() : violatedByLearner;
        UpdateUtils.multisetToUpdateAction(toPromote, delta, result);

        Multiset<Constraint> toSearch = violatedByTarget.isEmpty() ? tCandidate.getConstraints() : violatedByTarget;
        Constraint maxViolatedByTarget = UpdateUtils.getMax(toSearch, con);
        if (maxViolatedByTarget == null) {
            System.err.println("How can this?");
        }
        double maxVblValue = con.getRanking(maxViolatedByTarget);

        Multiset<Constraint> toDemote = getRankedAbove(maxVblValue, violatedByTarget, con);
        UpdateUtils.multisetToUpdateAction(toDemote, -delta, result);
        for (Constraint c : result.getRankingDeltas().keySet()) {
            if (c == null) {
                System.err.println("This constraint is null?");
            }
        }


        return result;
    }

    private Multiset<Constraint> getRankedAbove(double maxViolatedByLearner,
                                                Multiset<Constraint> violatedByTarget, Hierarchy con) {
        Multiset<Constraint> result = HashMultiset.create();
        for (Constraint constraint : violatedByTarget) {
            double value = con.getRanking(constraint);
            if (value > maxViolatedByLearner) {
                result.add(constraint);
            }
        }
        if (result.isEmpty()) {
            result.add(UpdateUtils.getMax(violatedByTarget, con));
        }
        return result;

    }

    @Override
    public String toString() {
        return "AllUpHighDown";
    }
}
