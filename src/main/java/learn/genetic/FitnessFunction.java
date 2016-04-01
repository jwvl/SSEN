/**
 *
 */
package learn.genetic;

import com.google.common.base.Function;

/**
 * @author jwvl
 * @date 30/09/2015
 */
public class FitnessFunction implements Function<ConHypothesis, Double> {
    private final int fakeSuccesses;
    private final int initialBias;


    /**
     * @param initialBias
     */
    public FitnessFunction(int initialBias, double initialSuccessRate) {
        this.initialBias = initialBias;
        this.fakeSuccesses = (int) (initialSuccessRate * initialBias);
    }


    /* (non-Javadoc)
     * @see com.google.common.base.Function#apply(java.lang.Object)
     */
    @Override
    public Double apply(ConHypothesis arg0) {
        return (fakeSuccesses + arg0.getSuccessfulEvaluations()) / (double) (initialBias + arg0.getNumEvaluations());
    }

}
