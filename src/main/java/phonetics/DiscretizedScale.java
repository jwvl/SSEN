/**
 *
 */
package phonetics;


import forms.primitives.feature.ScaleFeature;

/**
 * @author jwvl
 * @date 04/10/2015
 */
public class DiscretizedScale implements Comparable<DiscretizedScale> {
    private final Measure measure;
    private final double minValue;
    private final double maxValue;
    private final double stepSize;
    private final double scaleLength;
    private final int numSteps;
    private final ScaleFeature[] values;
    private final int count;
    private static int counter = 0;


    /**
     * @param minValue

     */
    public DiscretizedScale(Measure measure, double minValue, double stepSize, int numSteps) {
        this.measure = measure;
        this.minValue = minValue;
        this.stepSize = stepSize;
        this.numSteps = numSteps;
        this.maxValue = minValue + (stepSize * (numSteps - 1));
        this.scaleLength = maxValue - minValue;
        this.values = new ScaleFeature[numSteps];
        for (int i=0; i < numSteps; i++) {
            values[i] = ScaleFeature.createInstance(this,i);
        }
        count = counter++;
    }

    /**
     * @param minValue

     */
    public DiscretizedScale(Measure measure, double minValue, double maxValue, double stepSize) {
        this.measure = measure;
        this.minValue = minValue;
        this.stepSize = stepSize;
        this.maxValue = maxValue;
        this.scaleLength = maxValue - minValue;
        this.numSteps = 1 + ((int) (scaleLength / stepSize));
        this.values = new ScaleFeature[numSteps];
        for (int i=0; i < numSteps; i++) {
            values[i] = ScaleFeature.createInstance(this,i);
        }
        count = counter++;
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
    public int getNumSteps() {
        return numSteps;
    }

    public ScaleFeature getFeatureForValue(int step) {
        step = Math.max(0,step);
        step = Math.min(numSteps-1,step);
        return values[step];
    }

    public ScaleFeature[] getValues() {
        return values;
    }

    @Override
    public int compareTo(DiscretizedScale o) {
        return count - o.count;
    }

    public double getStepSize() {
        return stepSize;
    }

    public ScaleFeature getForValue(double value) {
        int stepNumber = getStepIndex(value);
        if (stepNumber >= values.length) {
            System.err.println("Huh?");
        }
        return getFeatureForValue(stepNumber);
    }
}
