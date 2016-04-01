/**
 *
 */
package learn.update;

import ranking.GrammarHierarchy;
import ranking.constraints.Constraint;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public class UpdateAction {
    private final Map<Constraint, Double> rankingDeltas;
    public static final UpdateAction NO_UPDATE = new UpdateAction(Collections.EMPTY_MAP);
    private static char UP_SYMBOL = '↑', DOWN_SYMBOL = '↓';

    private UpdateAction(Map<Constraint, Double> rankingDeltas) {
        this.rankingDeltas = rankingDeltas;
    }

    public static UpdateAction create() {
        return new UpdateAction(new IdentityHashMap<>());
    }

    public void put(Constraint constraint, double value) {
        rankingDeltas.put(constraint, value);
    }

    public double getDelta(Constraint constraint) {
        Double result = rankingDeltas.get(constraint);
        if (result == null) {
            return 0;
        }
        return result;
    }

    public void add(Constraint constraint, double delta) {
        double oldValue = getDelta(constraint);
        put(constraint, oldValue + delta);
    }

    public void updateRanking(GrammarHierarchy grammarHierarchy) {
        for (Constraint constraint : rankingDeltas.keySet()) {
            double delta = rankingDeltas.get(constraint);
            grammarHierarchy.updateConstraintRanking(constraint, delta);
        }
    }


    public String toPrettyString() {
        StringBuilder builder = new StringBuilder("UpdateAction:\n");
        for (Constraint constraint : rankingDeltas.keySet()) {
            double delta = rankingDeltas.get(constraint);
            if (delta < 0) {
                builder.append(DOWN_SYMBOL);
            } else {
                builder.append(UP_SYMBOL);
            }
            builder.append(String.format("   %s : %s%n", constraint, Math.abs(delta)));
        }
        return builder.toString();
    }

    public Map<Constraint, Double> getRankingDeltas() {
        return rankingDeltas;
    }
}
