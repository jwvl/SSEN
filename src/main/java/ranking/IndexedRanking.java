package ranking;

import com.typesafe.config.ConfigFactory;
import ranking.constraints.Constraint;
import ranking.constraints.RankedConstraint;

import java.util.List;

/**
 * Created by janwillem on 01/04/16.
 */
public class IndexedRanking {
    private final short[] indices;
    private final int maxSize = ConfigFactory.load().getInt("implementation.expectedNumConstraints");

    // NB: Assumes that RankedConstraints is sorted & reversed!
    public IndexedRanking(List<RankedConstraint> rankedConstraints) {
        indices = new short[maxSize];
        for (short i=0; i < rankedConstraints.size(); i++) {
            short constraintIndex = rankedConstraints.get(i).getConstraint().getId();
            indices[constraintIndex] = i;
        }
    }

    public short getIndex(Constraint c) {
        return indices[c.getId()];
    }

}
