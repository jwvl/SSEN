package constraints.hierarchy.reimpl;

import com.typesafe.config.ConfigFactory;
import constraints.Constraint;
import constraints.RankedConstraint;

import java.util.List;

/**
 * Created by janwillem on 01/04/16.
 */
public class IndexedRanking {
    private final short[] indexToRanking;
    private final static int maxSize = ConfigFactory.load().getInt("implementation.expectedNumConstraints");

    // NB: Assumes that RankedConstraints is sorted & reversed!
    public IndexedRanking(List<RankedConstraint> rankedConstraints) {

        indexToRanking = new short[maxSize];
        short count = 1;
        for (RankedConstraint rankedConstraint: rankedConstraints) {
            short constraintIndex = rankedConstraint.getConstraint().getId();
            indexToRanking[constraintIndex] = count++;
        }
    }


    public short getRankingIndex(Constraint c) {

        int result = indexToRanking[c.getId()];
        if (result < 1) {
            System.err.printf("Constraint %s (%d)not in index?%n", c, c.getId());
            System.exit(0);

        }
        return indexToRanking[c.getId()];
    }
}
