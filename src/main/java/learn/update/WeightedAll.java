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
public class WeightedAll implements UpdateAlgorithm {

    /* (non-Javadoc)
     * @see learn.update.UpdateAlgorithm#update(java.util.Collection, java.util.Collection, double)
     */
    @Override
    public void update(Hierarchy con, Collection<ViolatedCandidate> violatedByL,
                       Collection<ViolatedCandidate> violatedByT, double delta) {
        Multiset<Constraint> tPreferring = violatedByL.iterator().next().getConstraints();
        Multiset<Constraint> lPreferring = violatedByT.iterator().next().getConstraints();
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
    @Override
    public UpdateAction getUpdate(Hierarchy con, ViolatedCandidate lCandidate,
                                  ViolatedCandidate tCandidate, double delta) {
        Multiset<Constraint> tPreferring = lCandidate.getConstraints();
        Multiset<Constraint> lPreferring = tCandidate.getConstraints();
        UpdateAction updateAction = UpdateAction.create();
        UpdateUtils.multisetToUpdateAction(lPreferring, delta, updateAction);
        UpdateUtils.multisetToUpdateAction(tPreferring, -delta, updateAction);
        return updateAction;
    }

}
