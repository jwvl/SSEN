/**
 *
 */
package ranking;

import com.google.common.collect.Multimap;
import grammar.levels.Level;

/**
 * @author jwvl
 * @date Dec 5, 2014
 */
public class LevelwiseConstraintMap {
    private Multimap<Level, OldRankedConstraint> interPerLevel;
    private Multimap<Level, OldRankedConstraint> intraPerLevel;

    private LevelwiseConstraintMap(OldHierarchy h) {
        // TODO Build it!
    }

}
