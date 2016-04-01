package ranking;

import com.google.common.collect.Maps;
import ranking.constraints.Constraint;
import ranking.constraints.RankedConstraint;

import java.util.*;

/**
 * Created by janwillem on 29/03/16.
 */
public abstract class Hierarchy implements Con {
    private Map<Constraint, Double> map;
    private boolean isConcurrent;

    public abstract double getRanking(Constraint c);

    public abstract void addConstraint(Constraint c, double value);

    public Hierarchy(boolean isConcurrent) {
        Map<Constraint, Double> identityMap = Maps.newIdentityHashMap();
        if (isConcurrent) {
            map = Collections.synchronizedMap(identityMap);
        } else {
            map = identityMap;
        }
        this.isConcurrent = isConcurrent;
    }

    public boolean isConcurrent() {
        return isConcurrent;
    }

    /* (non-Javadoc)
 * @see java.lang.Iterable#iterator()
 */
    public Iterator<Constraint> iterator() {
        return map.keySet().iterator();
    }

    /* (non-Javadoc)
     * @see ranking.Con#contains(ranking.constraints.Constraint)
     */
    public boolean contains(Constraint constraint) {
        return map.containsKey(constraint);
    }

    public void put(Constraint constraint, double value) {
        map.put(constraint, value);
    }

    public int size() {
        return map.size();
    }

    /**
     *
     */
    public void printContents() {
        for (RankedConstraint rc : toRankedConstraintList()) {
            System.out.println(rc);
        }
    }

    public List<RankedConstraint> toRankedConstraintList() {
        List<RankedConstraint> rankedList = new ArrayList<>(size());
        for (Constraint constraint : this) {
            rankedList.add(RankedConstraint.of(constraint, getRankingValue(constraint)));
        }
        Collections.sort(rankedList);
        Collections.reverse(rankedList);
        return rankedList;
    }

    /**
     * @return
     */
    public Map<Constraint, Double> getMap() {
        return map;
    }

    protected double getRankingValue(Constraint c) {
        return map.get(c);
    }
}
