/**
 *
 */
package forms.primitives.feature;

import forms.phon.numerical.PhoneticElement;
import phonetics.DiscretizedScale;
import phonetics.Measure;

/**
 * @author jwvl
 * @date 04/10/2015
 */
public class ScaleFeature extends RangeFeature implements PhoneticElement, Comparable<ScaleFeature> {
    private final DiscretizedScale scale;

    /**
     * @param v
     */
    protected ScaleFeature(DiscretizedScale scale, Integer v) {
        super(scale.getMeasure().toString(), v);
        this.scale = scale;
    }

    public static ScaleFeature createInstance(DiscretizedScale scale, int v) {
        return new ScaleFeature(scale, v);
    }

    public static ScaleFeature getInstanceFromStep(DiscretizedScale scale, int v) {
        return scale.getFeatureForValue(v);
    }

    public static ScaleFeature getInstanceFromValue(DiscretizedScale scale, double v) {
        int step = scale.getStepIndex(v);
        return getInstanceFromStep(scale, step);
    }

    @Override
    public int size() {
        return 1;
    }

    public String toString() {
        StringBuilder result = new StringBuilder(scale.getMeasure().getScaleName());
        result.append(" = ").append(scale.getValue(this.getValue()));
        result.append(" ").append(scale.getMeasure().getUnitName());
        return result.toString();
    }

    @Override
    public double getRelativeValue() {
        return intValue() / (double) (scale.getNumSteps()-1);
    }

    public Measure getMeasure() {
        return scale.getMeasure();
    }

    @Override
    public int compareTo(ScaleFeature o) {
        return scale.compareTo(o.scale);
    }
}
