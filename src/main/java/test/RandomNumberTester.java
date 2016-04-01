package test;

import eval.sample.AbstractSampler;
import eval.sample.GaussianRandomSampler;
import eval.sample.GaussianXORSampler;
import eval.sample.GaussianXSSampler;
import util.debug.Stopwatch;

/**
 * @author jwvl
 * @date 08/11/2014
 */
public class RandomNumberTester {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int numNumbers = 10000000;
        AbstractSampler gs = GaussianXORSampler.createInstance(1.0);
        AbstractSampler gs2 = GaussianRandomSampler.createInstance(1.0);
        AbstractSampler gs3 = GaussianXSSampler.createInstance(1.0);
        Stopwatch.start();
        for (int i = 0; i < numNumbers; i++) {
            gs.sampleDouble();
        }
        Stopwatch.reportElapsedTime("XOR shift generation:", true);
        for (int i = 0; i < numNumbers; i++) {
            gs2.sampleDouble();
        }
        Stopwatch.reportElapsedTime("java.util.random generation:", true);

        for (int i = 0; i < numNumbers; i++) {
            gs3.sampleDouble();
        }
        Stopwatch.reportElapsedTime("Random XS generation:", true);
    }

}
