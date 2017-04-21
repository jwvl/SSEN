/**
 *
 */
package forms.primitives.feature;

import java.util.Collection;
import java.util.Objects;

/**
 * @author jwvl
 * @date May 25, 2015
 */
public abstract class AbstractMFeature extends Feature<String> {


    /**
     * @param attribute
     * @param
     */
    protected AbstractMFeature(String attribute, String value) {
        super(attribute, value);
        System.out.printf("Created abstract m-feature %s:%s%n", attribute, value);
    }

    public boolean valueEquals(AbstractMFeature other) {
        return this.getValue().equals(other.getValue());
    }

    public abstract Collection<String> getValueSet(boolean includeNull);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMFeature that = (AbstractMFeature) o;
        return Objects.equals(getAttribute(), that.getAttribute()) &&
                Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAttribute(), getValue());
    }
}
