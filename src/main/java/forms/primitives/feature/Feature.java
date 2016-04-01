/**
 *
 */
package forms.primitives.feature;


/**
 * A Feature is an attribute-value pair.
 * Attribute is a String, Values can be anything.
 *
 * @author jwvl
 * @date Dec 12, 2014
 */
public abstract class Feature<V extends Object> {

    private final String attribute;
    private final V value;

    protected Feature(String attribute, V v) {
        this.attribute = attribute;
        value = v;
    }


    public String getAttribute() {
        return attribute;
    }

    public V getValue() {
        return value;
    }

    public abstract Feature<V> createNullValue();

    public abstract boolean isNull();

    @Override
    public abstract String toString();

    public boolean attributeEquals(Feature<V> other) {
        return this.getAttribute().equals(other.getAttribute());
    }

    public abstract boolean expressesValue();


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((attribute == null) ? 0 : attribute.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Feature))
            return false;
        @SuppressWarnings("rawtypes")
        Feature other = (Feature) obj;
        if (attribute == null) {
            if (other.attribute != null)
                return false;
        } else if (!attribute.equals(other.attribute))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }


}
