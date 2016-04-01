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
public class WeightedAll implements UpdateAlgorithm {

    /* (non-Javadoc)
     * @see learn.update.UpdateAlgorithm#update(java.util.Collection, java.util.Collection, double)
     */
    @Override
    public void update(GrammarHierarchy con, Collection<ViolatedCandidate> violatedByL,
                       Collection<ViolatedCandidate> violatedByT, double delta) {
        Multiset<Constraint> tPreferring = violatedByL.iterator().next().getConstraints();
        Multiset<Constraint> lPreferring = violatedByT.iterator().next().getConstraints();
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
    @Override
    public UpdateAction getUpdate(GrammarHierarchy con, ViolatedCandidate lCandidate,
                                  ViolatedCandidate tCandidate, double delta) {
        Multiset<Constraint> tPreferring = lCandidate.getConstraints();
        Multiset<Constraint> lPreferring = tCandidate.getConstraints();
        UpdateAction updateAction = UpdateAction.create();
        UpdateUtils.multisetToUpdateAction(lPreferring, delta, updateAction);
        UpdateUtils.multisetToUpdateAction(tPreferring, -delta, updateAction);
        return updateAction;
    }

}
