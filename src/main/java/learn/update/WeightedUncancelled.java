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
public class WeightedUncancelled implements UpdateAlgorithm {

    /* (non-Javadoc)
     * @see learn.update.UpdateAlgorithm#update(java.util.Collection, java.util.Collection, double)
     */
    @Override
    public void update(GrammarHierarchy con, Collection<ViolatedCandidate> violatedByL,
                       Collection<ViolatedCandidate> violatedByT, double delta) {
        ViolatedCandidate vblFirst = violatedByL.iterator().next();
        ViolatedCandidate vbtFirst = violatedByT.iterator().next();
        Multiset<Constraint> tPreferring = UpdateUtils.getViolatedByLearner(vblFirst, vbtFirst);
        Multiset<Constraint> lPreferring = UpdateUtils.getViolatedByTarget(vblFirst, vbtFirst);
        double weightedPromotionDelta = delta / tPreferring.size();
        for (Constraint constraint : tPreferring) {
            con.updateConstraintRanking(constraint, weightedPromotionDelta);
        }
        double weightedDemotionDelta = -delta / lPreferring.size();
        for (Constraint constraint : lPreferring) {
            con.updateConstraintRanking(constraint, weightedDemotionDelta);
        }

    }

    /* (non-Javadoc)
     * @see learn.update.UpdateAlgorithm#getUpdate(ranking.ConRankingMap, java.util.Collection, java.util.Collection, double)
     */
    /* (non-Javadoc)
     * @see learn.update.UpdateAlgorithm#getUpdate(ranking.ConRankingMap, java.util.Collection, java.util.Collection, double)
	 */
    @Override
    public UpdateAction getUpdate(GrammarHierarchy con, ViolatedCandidate lCandidate,
                                  ViolatedCandidate tCandidate, double delta) {
        UpdateAction updateAction = UpdateAction.create();
        Multiset<Constraint> tPreferring = UpdateUtils.getViolatedByLearner(lCandidate, tCandidate);
        Multiset<Constraint> lPreferring = UpdateUtils.getViolatedByTarget(lCandidate, tCandidate);
        UpdateUtils.multisetToUpdateAction(lPreferring, -delta, updateAction);
        UpdateUtils.multisetToUpdateAction(tPreferring, delta, updateAction);
        return updateAction;
    }


}
