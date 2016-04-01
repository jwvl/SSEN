/**
 *
 */
package forms.primitives.feature;

import phonetics.DiscretizedScale;

/**
 * @author jwvl
 * @date 04/10/2015
 */
public class ScaleFeature extends IntegerFeature {
    private final DiscretizedScale scale;

    /**
     * @param attribute
     * @param v
     */
    protected ScaleFeature(DiscretizedScale scale, Integer v) {
        super(scale.getMeasure().toString(), v);
        this.scale = scale;
    }

    public static ScaleFeature createInstance(DiscretizedScale scale, int v) {
        return new ScaleFeature(scale, v);
    }

    public String toString() {
        StringBuilder result = new StringBuilder(scale.getMeasure().getScaleName());
        result.append(" = ").append(scale.getValue(this.getValue()));
        result.append(" ").append(scale.getMeasure().getUnitName());
        return result.toString();
    }

}
