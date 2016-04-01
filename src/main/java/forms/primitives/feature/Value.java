/**
 *
 */
package forms.primitives.feature;

/**
 * A Value class goes with a Feature. Values can be numeral, binary, depending
 * on the implementation.
 *
 * @author jwvl
 * @date Dec 14, 2014
 */
public abstract class Value<V extends Value<V>> {

    public abstract String toString();

    public abstract int hashCode();

    public abstract boolean equals(Object o);

    public abstract boolean isNull();

    public abstract V getNull();

}
