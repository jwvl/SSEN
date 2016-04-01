/**
 *
 */
package learn.genetic;

import java.util.List;

/**
 * @author jwvl
 * @date 30/09/2015
 */
public interface MemberPicker {

    /**
     * @param population
     * @return
     */
    List<ConHypothesis> pick(CandidatePool candidatePool, int numMembers, boolean fittest);

}
