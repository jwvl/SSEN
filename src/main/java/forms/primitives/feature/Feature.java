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


}
