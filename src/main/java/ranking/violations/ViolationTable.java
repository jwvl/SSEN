/**
 *
 */
package ranking.violations;

import candidates.Candidate;
import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;
import ranking.OldRankedConstraint;

import java.util.List;

/**
 * @author jwvl
 * @date 20/02/2016
 * A (sorted?)
 */
public class ViolationTable {
    private Table<OldRankedConstraint, Candidate, Integer> profiles;

    /**
     * @param profiles
     */
    private ViolationTable() {
        this.profiles = TreeBasedTable.create(OldRankedConstraint.DisharmonyComparator, Candidate.AlphabeticOrdering);
    }

    public void addViolations(List<RankedViolation> violations, Candidate candidate) {
        for (RankedViolation rankedViolation : violations) {
            profiles.put(rankedViolation.getRankedConstraint(), candidate, rankedViolation.getValue());
        }
    }


}
