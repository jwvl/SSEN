/**
 *
 */
package ranking;

import com.google.common.collect.Maps;
import ranking.constraints.Constraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jwvl
 * @date Sep 4, 2015
 * An "implementation" of CON. Translates a Constraint object to a RankedConstraint,
 * and adds new RankedConstraints if necessary.
 * Synchronized for used in multithreaded evaluation.
 */
public class SynchronizedConMap {
    private static int DEFAULT_STRATUM = 0;
    private static double DEFAULT_INITIAL_RANKING = 100.0;
    private static double DEFAULT_INITIAL_PLASTICITY = 1.0; //TODO Check if this is actually ever used
    private Map<Constraint, OldRankedConstraint> map;

    public SynchronizedConMap() {
        map = Maps.newIdentityHashMap();
    }

    private synchronized void addNewRankedConstraint(Constraint constraint) {
        OldRankedConstraint toAdd = new OldRankedConstraint(constraint);
        toAdd.setStratum(DEFAULT_STRATUM);
        toAdd.setRankingValue(DEFAULT_INITIAL_RANKING);
        toAdd.setDisharmony(DEFAULT_INITIAL_RANKING);
        map.put(constraint, toAdd);
    }

    public synchronized OldRankedConstraint getRankedConstraint(Constraint constraint) {
        if (!map.containsKey(constraint))
            addNewRankedConstraint(constraint);
        return map.get(constraint);
    }

    public Collection<OldRankedConstraint> getRankedConstraints(List<Constraint> constraints) {
        List<OldRankedConstraint> result = new ArrayList<OldRankedConstraint>(constraints.size());
        for (Constraint constraint : constraints) {
            result.add(getRankedConstraint(constraint));
        }
        return result;
    }

    /**
     * @param constraints
     * @return
     */
    public List<OldRankedConstraint> getRankedConstraints(Constraint[] constraints) {
        List<OldRankedConstraint> result = new ArrayList<OldRankedConstraint>(constraints.length);
        for (Constraint constraint : constraints) {
            result.add(getRankedConstraint(constraint));
        }
        return result;
    }

    /**
     *
     */
    public void printContents() {
        for (Constraint c : map.keySet()) {
            System.out.println(map.get(c).toStringWithRanking());
        }

    }


}
