package phonetics.predefined;

import phonetics.DiscretizedScale;
import phonetics.Measure;

/**
 * Created by janwillem on 29/04/16.
 */
public class Formant {

    public static DiscretizedScale createFormantScale(int formantNumber, double minValue, double maxValue, int numSteps) {
        double stepSize = (maxValue-minValue)/(double)numSteps-1;
        return createFormantScale(formantNumber,minValue,maxValue,stepSize);
    }

    public static DiscretizedScale createFormantScale(int formantNumber, double minValue, double maxValue, double stepSize) {
        int numSteps = 1+(int) ((maxValue-minValue)/stepSize);
        String scaleName = "F"+formantNumber;
        String unitName = "Bark";
        Measure measure = new Measure(scaleName, unitName);
        return new DiscretizedScale(measure, minValue, stepSize, numSteps);
    }
}
