package forms.phon.numerical;

import forms.primitives.feature.ScaleFeature;
import phonetics.DiscretizedScale;

/**
 * Created by janwillem on 07/07/16.
 */
public class ScalarFeatureSpace {
    private final DiscretizedScale[] scales;
    private final int[] multipliers;
    private final int totalSize;

    public ScalarFeatureSpace(DiscretizedScale[] scales) {
        this.scales = scales;
        multipliers = new int[scales.length];
        multipliers[0] = 1;
        int totalSizeAccumulator = scales[0].getNumSteps();
        for (int i=1; i < scales.length; i++) {
            int nextScale = scales[i].getNumSteps();
            totalSizeAccumulator *= nextScale;
            multipliers[i] = multipliers[i-1]*nextScale;
        }
        totalSize = totalSizeAccumulator;
    }

    public int encodeIndices(int[] indices) {
        int result = 0;
        for (int i=0; i < scales.length; i++) {
            int nextValue = indices[i]*multipliers[i];
            result +=nextValue;
        }
        return result;
    }

    public int encode(ScaleFeature[] features) {
        int[] asArray = new int[features.length];
        for (int i=0; i < features.length; i++) {
            asArray[i] = features[i].getValue();
        }
        return encodeIndices(asArray);
    }

    public ScaleFeature[] decode(int index) {
        int[] indices = decodeToIndices(index);
        ScaleFeature[] result = new ScaleFeature[scales.length];
        for (int i = 0; i < indices.length; i++) {
            result[i] = scales[i].getFeatureForValue(indices[i]);
        }
        return result;
    }

    public int[] decodeToIndices(int index) {
        int rest = index;
        int[] result = new int[scales.length];
        for (int i = scales.length - 1; i > 0; i--) {
            int whole = rest / multipliers[i];
            rest = index % multipliers[i];
            result[i] = whole;
        }
        result[0] = rest;
        return result;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public int getDimension() {
        return scales.length;
    }
}
