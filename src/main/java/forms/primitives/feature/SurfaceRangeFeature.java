package forms.primitives.feature;

import java.util.ArrayList;
import java.util.List;

public class SurfaceRangeFeature extends RangeFeature {

    private final double max;
    private final String valueName;

    public static List<SurfaceRangeFeature> fromNameAndValueList(String attribute, List<String> values) {
        List<SurfaceRangeFeature> result = new ArrayList<>();
        double max = values.size()-1;
        for (int i=0; i < values.size(); i++) {
            String valueName = values.get(i);
            result.add(new SurfaceRangeFeature(attribute,i,max,valueName));
        }
        return result;
    }

    public SurfaceRangeFeature(String attribute, int value, double max, String valueName) {
        super(attribute, value);
        this.max = max;
        this.valueName = valueName;
    }

    public SurfaceRangeFeature(String attribute, int value, double max) {
        super(attribute, value);
        this.max = max;
        this.valueName = String.format("%.2f",getRelativeValue());
    }

    @Override
    public double getRelativeValue() {
        return intValue() / max;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(getAttribute());
        sb.append("[").append(valueName).append("]");
        return sb.toString();
    }
}
