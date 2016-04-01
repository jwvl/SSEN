/**
 *
 */
package eval.sample;

import org.uncommons.maths.random.ContinuousUniformGenerator;
import org.uncommons.maths.random.XORShiftRNG;

import java.util.Random;

/**
 * Implementation of a Sampler using a Gaussian (= normal) distribution.
 *
 * @author jwvl
 * @date Dec 5, 2014
 */
public class UniformRandomSampler extends AbstractSampler {
    private ContinuousUniformGenerator gg;
    private static Random defaultRandom = new XORShiftRNG();

    private UniformRandomSampler(double mu, double sigma, Random r) {
        this.gg = new ContinuousUniformGenerator(mu - sigma / 2.0, mu + sigma / 2.0, defaultRandom);
    }

    private UniformRandomSampler(double mu, double sigma) {
        this(mu, sigma, defaultRandom);
    }

    private UniformRandomSampler(double sigma) {
        this(0d, sigma, defaultRandom);
    }

    public double sampleDouble() {
        return gg.nextValue();
    }

    /**
     * @param d
     * @return
     */
    public static AbstractSampler createInstance(double d) {
        return new UniformRandomSampler(d);
    }

}
