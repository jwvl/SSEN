package constraints.hierarchy.reimpl;

import com.typesafe.config.ConfigFactory;
import constraints.Constraint;
import constraints.RankedConstraint;

import java.util.List;

/**
 * Created by janwillem on 01/04/16.
 */
public class IndexedRanking {
    private final short[] rankingsByIndex;
    private final short[] indexByRanking;
    private final Hierarchy parentHierarchy;
    private final static int maxSize = ConfigFactory.load().getInt("implementation.expectedNumConstraints");

    // NB: Assumes that RankedConstraints is sorted & reversed!
    public IndexedRanking(List<RankedConstraint> rankedConstraints, Hierarchy parentHierarchy) {
        this.parentHierarchy = parentHierarchy;
        rankingsByIndex = new short[maxSize];
        indexByRanking = new short[maxSize];
        for (short i=0; i < rankedConstraints.size(); i++) {
            short constraintIndex = rankedConstraints.get(i).getConstraint().getId();
            rankingsByIndex[constraintIndex] = i;
            indexByRanking[i] = constraintIndex;
        }
    }


    public short getRankingIndex(Constraint c) {

        int result = rankingsByIndex[c.getId()];
        if (result < 1) {

            System.out.println("Constraint not in index?");
            rankingsByIndex[c.getId()] = c.getId();
            parentHierarchy.addConstraint(c);
        }
        return rankingsByIndex[c.getId()];
    }

    public short getConstraintIndex(short rankingIndex) {
        return indexByRanking[rankingIndex];
    }


}
