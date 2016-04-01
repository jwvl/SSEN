/**
 *
 */
package eval.sample;

import org.uncommons.maths.random.GaussianGenerator;

import java.util.Random;

/**
 * Implementation of a Sampler using a Gaussian (= normal) distribution.
 *
 * @author jwvl
 * @date Dec 5, 2014
 */
public class GaussianXSSampler extends AbstractSampler {
    private GaussianGenerator gg;
    private static Random defaultRandom = new util.math.XSRandom(1L);

    private GaussianXSSampler(double mu, double sigma, Random r) {
        this.gg = new GaussianGenerator(mu, sigma, r);
    }

    private GaussianXSSampler(double mu, double sigma) {
        this(mu, sigma, defaultRandom);
    }

    private GaussianXSSampler(double sigma) {
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
        return new GaussianXSSampler(d);
    }

}
