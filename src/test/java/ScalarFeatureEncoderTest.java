import forms.phon.numerical.ScalarFeatureSpace;
import forms.primitives.feature.ScaleFeature;
import phonetics.DiscretizedScale;
import phonetics.predefined.Formant;

import java.util.Arrays;

/**
 * Created by janwillem on 07/07/16.
 */
public class ScalarFeatureEncoderTest {


    public static void main(String[] args) {
        DiscretizedScale f1 = Formant.createFormantScale(1, 2.0, 8.0, 0.25);
        DiscretizedScale f2 = Formant.createFormantScale(2, 6.0, 16.0, 0.5);
        DiscretizedScale[] asArray = new DiscretizedScale[]{f1, f2};
        ScalarFeatureSpace scalarFeatureEncoder = new ScalarFeatureSpace(asArray);
        int[] asIntArray = {0, 1};
        int encoded = scalarFeatureEncoder.encodeIndices(asIntArray);
        System.out.println(encoded);
        int[] unencoded = scalarFeatureEncoder.decodeToIndices(encoded);
        System.out.println(Arrays.toString(unencoded));
        ScaleFeature[] scaleFeatures = scalarFeatureEncoder.decode(encoded);
        System.out.println(Arrays.toString(scaleFeatures));
    }
}
