/**
 *
 */
package forms.primitives.feature;

/**
 * A Feature whose value is a String. Equality is decided by string equality. In
 * other words, just a wrapper for a String with some added Feature
 * functionality..
 *
 * @author jwvl
 * @date Dec 14, 2014
 */
public class StringValue extends Value<StringValue> {
    private final String value;

    private StringValue(String val) {
        this.value = val;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof StringValue))
            return false;
        StringValue other = (StringValue) obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * @param valueAsString
     * @return A new StringValue with the corresponding String
     */
    public static StringValue createInstance(String valueAsString) {
        return new StringValue(valueAsString);
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Value#isNull()
     */
    @Override
    public boolean isNull() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Value#getNull()
     */
    @Override
    public StringValue getNull() {
        // TODO Auto-generated method stub
        return null;
    }

}
