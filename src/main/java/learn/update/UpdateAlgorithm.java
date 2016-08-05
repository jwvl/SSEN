/**
 *
 */
package learn.update;

import constraints.hierarchy.reimpl.Hierarchy;
import learn.ViolatedCandidate;

import java.util.Collection;

/**
 * @author jwvl
 * @date Aug 22, 2015
 */
public interface UpdateAlgorithm {

    void update(Hierarchy con, Collection<ViolatedCandidate> lCandidates, Collection<ViolatedCandidate> tCandidates, double delta);

    UpdateAction getUpdate(Hierarchy con, ViolatedCandidate lCandidate, ViolatedCandidate tCandidate, double delta);


}
