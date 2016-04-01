/**
 *
 */
package phonetics;

import forms.primitives.feature.ScaleFeature;

/**
 * @author jwvl
 * @date 03/12/2015
 */
public class FeatureScale {
    private final DiscretizedScale scale;
    private final ScaleFeature[] features;

    private FeatureScale(DiscretizedScale scale) {
        this.scale = scale;
        this.features = instantiateFeatures();
    }

    /**
     * @return
     */
    private ScaleFeature[] instantiateFeatures() {
        ScaleFeature[] result = new ScaleFeature[scale.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = ScaleFeature.createInstance(scale, i);
        }
        return result;
    }

    public ScaleFeature getByIndex(int index) {
        return features[index];
    }

    public ScaleFeature getByValue(double value) {
        int stepIndex = scale.getStepIndex(value);
        return getByIndex(stepIndex);
    }

}
