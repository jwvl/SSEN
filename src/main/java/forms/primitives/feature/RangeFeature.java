package forms.primitives.feature;

public abstract class RangeFeature extends IntegerFeature {
    /**
     * @param attribute
     * @param v
     */
    protected RangeFeature(String attribute, Integer v) {
        super(attribute, v);
    }

    public abstract double getRelativeValue();
}
