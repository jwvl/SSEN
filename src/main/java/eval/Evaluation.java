/**
 *
 */
package eval;

import learn.ViolatedCandidate;


/**
 * @author jwvl
 * @date Nov 25, 2014
 */
public interface Evaluation {

    /**
     * @return
     */
    long getId();

    ViolatedCandidate getWinner();


}
