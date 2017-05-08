/**
 *
 */
package eval.sample;

import org.uncommons.maths.random.GaussianGenerator;
import org.uncommons.maths.random.XORShiftRNG;

import java.util.Random;

/**
 * Implementation of a Sampler using a Gaussian (= normal) distribution.
 *
 * @author jwvl
 * @date Dec 5, 2014
 */
public class GaussianXORSampler extends AbstractSampler {
    private GaussianGenerator gg;
    private static Random defaultRandom = new XORShiftRNG();
    private static GaussianXORSampler lastSampler;
    private static double lastDouble = 0;

    private GaussianXORSampler(double mu, double sigma, Random r) {
        this.gg = new GaussianGenerator(mu, sigma, r);
    }

    private GaussianXORSampler(double mu, double sigma) {
        this(mu, sigma, defaultRandom);
    }

    private GaussianXORSampler(double sigma) {
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
        if (d != lastDouble) {
            lastSampler = new GaussianXORSampler(d);
            lastDouble = d;
        }
        return lastSampler;
    }

}
