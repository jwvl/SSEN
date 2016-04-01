/**
 *
 */
package learn.update;

import learn.ViolatedCandidate;
import ranking.GrammarHierarchy;

import java.util.Collection;

/**
 * @author jwvl
 * @date Aug 22, 2015
 */
public interface UpdateAlgorithm {

    void update(GrammarHierarchy con, Collection<ViolatedCandidate> lCandidates, Collection<ViolatedCandidate> tCandidates, double delta);

    UpdateAction getUpdate(GrammarHierarchy con, ViolatedCandidate lCandidate, ViolatedCandidate tCandidate, double delta);


}
