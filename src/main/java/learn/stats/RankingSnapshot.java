/**
 *
 */
package learn.stats;

import constraints.Constraint;
import constraints.hierarchy.reimpl.Hierarchy;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public class RankingSnapshot {
    private final int atLearningStep;
    private final Map<Constraint, Double> snapshot;

    /**
     */
    private RankingSnapshot(int atLearningStep) {
        this.atLearningStep = atLearningStep;
        this.snapshot = new IdentityHashMap<Constraint, Double>();
    }

    private void add(Constraint constraint, double value) {
        snapshot.put(constraint, value);
    }

    public static RankingSnapshot create(int learningStep, Hierarchy hierarchy) {
        RankingSnapshot result = new RankingSnapshot(learningStep);
        for (Constraint constraint : hierarchy) {
            double rankingValue = hierarchy.getRanking(constraint);
            result.add(constraint, rankingValue);
        }
        return result;
    }

}
