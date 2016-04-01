/**
 *
 */
package forms.primitives.feature;

/**
 * @author jwvl
 * @date Dec 14, 2014
 */
public class PositiveValue extends Value<PositiveValue> {
    protected final int val;
    protected PositiveValue NULL = new PositiveValue(0);

    /**
     * @param val
     */
    private PositiveValue(int val) {
        this.val = val;
        val = 0;
    }

    public PositiveValue getInstance(int value) {
        if (value < 1) {
            return NULL;
        } else {
            return new PositiveValue(value);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + val;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PositiveValue))
            return false;
        PositiveValue other = (PositiveValue) obj;
        if (isNull() != other.isNull())
            return false;
        return val == other.val;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

    /**
     * @param value The (positive integer) to assign
     * @return A PositiveValue object
     */
    public static PositiveValue create(int value) {
        return new PositiveValue(value);
    }

    /**
     * Returns a primitive int representing the value of this instance.
     *
     * @return The positive value of this Value as an int
     */
    public int getValueAsInt() {
        return val;
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Value#isNull()
     */
    @Override
    public boolean isNull() {
        return (this == NULL);
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Value#getNull()
     */
    @Override
    public PositiveValue getNull() {
        return NULL;
    }

}
