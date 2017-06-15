package eval.sample;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

/**
 * Created by janwillem on 07/05/2017.
 */
public class XoroShiroSampler extends AbstractSampler {
    private final XoRoShiRo128PlusRandom random;
    private final double sigma;
    private final boolean sigmaIsOne;

    public XoroShiroSampler(double sigma) {
        random = new XoRoShiRo128PlusRandom();
        this.sigma = sigma;
        sigmaIsOne = sigma == 1.0;
    }


    @Override
    public double sampleDouble() {
        if (sigmaIsOne) {
            return random.nextGaussian();
        } else {
            return random.nextGaussian()*sigma;
        }
    }
}
