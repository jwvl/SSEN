/**
 *
 */
package forms.primitives.feature;

/**
 * @author jwvl
 * @date 02/10/2015
 */
public class IntegerFeature extends Feature<Integer> {

    /**
     * @param attribute
     * @param v
     */
    protected IntegerFeature(String attribute, Integer v) {
        super(attribute, v);
    }

    public int getDistance(IntegerFeature other) {
        return Math.abs(this.getValue() - other.getValue());
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Feature#createNullValue()
     */
    @Override
    public Feature<Integer> createNullValue() {
        return new IntegerFeature(this.getAttribute(), Integer.MIN_VALUE);
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Feature#isNull()
     */
    @Override
    public boolean isNull() {
        return getValue().equals(Integer.MIN_VALUE);
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Feature#toString()
     */
    @Override
    public String toString() {
        return getAttribute() + "=" + getValue();
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Feature#expressesValue()
     */
    @Override
    public boolean expressesValue() {
        return !isNull();
    }

}
