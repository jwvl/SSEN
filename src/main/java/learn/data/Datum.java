/**
 *
 */
package learn.data;

import candidates.AbstractInput;
import candidates.Candidate;

/**
 * @author jwvl
 * @date 13/02/2016
 */
public class Datum {
    private final AbstractInput input;
    private final Candidate output;

    /**
     * @param input  The input
     * @param output The output as a candidate
     */
    private Datum(AbstractInput input, Candidate output) {
        this.input = input;
        this.output = output;
    }


}
