/**
 *
 */
package phonetics;


/**
 * @author jwvl
 * @date 04/10/2015
 */
public class DiscretizedScale {
    private final Measure measure;
    private final double minValue;
    private final double maxValue;
    private final double stepSize;
    private final double scaleLength;
    private final int numSteps;


    /**
     * @param minValue
     * @param unitSize
     * @param numUnits
     */
    DiscretizedScale(Measure measure, double minValue, double stepSize, int numSteps) {
        this.measure = measure;
        this.minValue = minValue;
        this.stepSize = stepSize;
        this.numSteps = numSteps;
        this.maxValue = minValue + (stepSize * (numSteps - 1));
        this.scaleLength = maxValue - minValue;

    }

    public double getValue(int stepNumber) {
        return minValue + (stepNumber * stepSize);
    }

    public int getStepIndex(double value) {
        if (value <= minValue) {
            return 0;
        } else if (value >= maxValue) {
            return numSteps - 1;
        } else {
            double normalized = value - minValue;
            double fraction = normalized / scaleLength;
            return (int) Math.round(fraction * numSteps);
        }
    }

    /**
     * @return
     */
    public Measure getMeasure() {
        return measure;
    }

    /**
     * @return
     */
    public int size() {
        return numSteps;
    }


}
