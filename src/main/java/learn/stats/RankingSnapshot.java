/**
 *
 */
package learn.stats;

import ranking.GrammarHierarchy;
import ranking.constraints.Constraint;

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
     * @param snapshot
     */
    private RankingSnapshot(int atLearningStep) {
        this.atLearningStep = atLearningStep;
        this.snapshot = new IdentityHashMap<Constraint, Double>();
    }

    private void add(Constraint constraint, double value) {
        snapshot.put(constraint, value);
    }

    public static RankingSnapshot create(int learningStep, GrammarHierarchy ranking) {
        RankingSnapshot result = new RankingSnapshot(learningStep);
        for (Constraint constraint : ranking) {
            double rankingValue = ranking.getStratifiedRanking(constraint).getValue();
            result.add(constraint, rankingValue);
        }
        return result;
    }

}
