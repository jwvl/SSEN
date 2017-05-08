package eval.sample;

import eval.harmony.autosort.StratifiedDouble;

/**
 * A Sampler object creates a stochastic constraints.ranking from a Hierarchy. In standard
 * GLA this is implemented with a Gaussian random number generator,
 * but other variants are thinkable as well (e.g. uniform random numbers)
 *
 * @author jwvl
 * @date Dec 5, 2014
 */
public abstract class AbstractSampler {

    public abstract double sampleDouble();

    /**
     * @return
     */
    public StratifiedDouble sampleStratifiedDouble(StratifiedDouble old) {
        double toAdd = sampleDouble();
        //	System.out.printf("Sampled %.3f%n",toAdd);
        return StratifiedDouble.of(old.getStratum(), old.getValue() + toAdd);
    }

    public double sampleDouble(double old) {
        return old + sampleDouble();
    }


}
