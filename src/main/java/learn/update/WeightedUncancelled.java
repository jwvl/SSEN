/**
 *
 */
package learn.update;

import com.google.common.collect.Multiset;
import constraints.Constraint;
import constraints.hierarchy.reimpl.Hierarchy;
import learn.ViolatedCandidate;

import java.util.Collection;

/**
 * @author jwvl
 * @date Aug 22, 2015
 */
public class WeightedUncancelled implements UpdateAlgorithm {

    /* (non-Javadoc)
     * @see learn.update.UpdateAlgorithm#update(java.util.Collection, java.util.Collection, double)
     */
    @Override
    public void update(Hierarchy con, Collection<ViolatedCandidate> violatedByL,
                       Collection<ViolatedCandidate> violatedByT, double delta) {
        ViolatedCandidate vblFirst = violatedByL.iterator().next();
        ViolatedCandidate vbtFirst = violatedByT.iterator().next();
        Multiset<Constraint> tPreferring = UpdateUtils.getViolatedByLearner(vblFirst, vbtFirst);
        Multiset<Constraint> lPreferring = UpdateUtils.getViolatedByTarget(vblFirst, vbtFirst);
        double weightedPromotionDelta = delta / tPreferring.size();
        for (Constraint constraint : tPreferring) {
            con.changeConstraintRanking(constraint, weightedPromotionDelta);
        }
        double weightedDemotionDelta = -delta / lPreferring.size();
        for (Constraint constraint : lPreferring) {
            con.changeConstraintRanking(constraint, weightedDemotionDelta);
        }

    }

    /* (non-Javadoc)
     * @see learn.update.UpdateAlgorithm#getUpdate(constraints.ranking.ConRankingMap, java.util.Collection, java.util.Collection, double)
     */
    /* (non-Javadoc)
     * @see learn.update.UpdateAlgorithm#getUpdate(constraints.ranking.ConRankingMap, java.util.Collection, java.util.Collection, double)
	 */
    @Override
    public UpdateAction getUpdate(Hierarchy con, ViolatedCandidate lCandidate,
                                  ViolatedCandidate tCandidate, double delta) {
        UpdateAction updateAction = UpdateAction.create();
        Multiset<Constraint> tPreferring = UpdateUtils.getViolatedByLearner(lCandidate, tCandidate);
        Multiset<Constraint> lPreferring = UpdateUtils.getViolatedByTarget(lCandidate, tCandidate);
        UpdateUtils.multisetToUpdateAction(lPreferring, -delta, updateAction);
        UpdateUtils.multisetToUpdateAction(tPreferring, delta, updateAction);
        return updateAction;
    }

    @Override
    public String toString() {
        return "WeightedUncancelled";
    }

}
