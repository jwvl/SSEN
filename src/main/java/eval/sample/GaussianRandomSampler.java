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
public class GaussianRandomSampler extends AbstractSampler {
    private GaussianGenerator gg;
    private static Random defaultRandom = new Random();

    private GaussianRandomSampler(double mu, double sigma, Random r) {
        this.gg = new GaussianGenerator(mu, sigma, r);
    }

    private GaussianRandomSampler(double mu, double sigma) {
        this(mu, sigma, defaultRandom);
    }

    private GaussianRandomSampler(double sigma) {
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
        return new GaussianRandomSampler(d);
    }

}
