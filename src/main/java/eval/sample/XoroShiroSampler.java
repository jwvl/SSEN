package eval.sample;

import it.unimi.dsi.util.XoRoShiRo128PlusRandom;

/**
 * Created by janwillem on 07/05/2017.
 */
public class XoroShiroSampler extends AbstractSampler {
    private final XoRoShiRo128PlusRandom random;
    private final double sigma;

    public XoroShiroSampler(double sigma) {
        random = new XoRoShiRo128PlusRandom();
        this.sigma = sigma;
    }


    @Override
    public double sampleDouble() {
        return random.nextGaussian()*sigma;
    }
}
