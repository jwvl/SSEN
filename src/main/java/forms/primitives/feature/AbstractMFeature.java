/**
 *
 */
package forms.primitives.feature;

import java.util.Collection;

/**
 * @author jwvl
 * @date May 25, 2015
 */
public abstract class AbstractMFeature extends Feature<String> {


    /**
     * @param attribute
     * @param v
     */
    protected AbstractMFeature(String attribute, String value) {
        super(attribute, value);
        System.out.printf("Created abstract m-feature %s:%s%n", attribute, value);
    }

    public boolean valueEquals(AbstractMFeature other) {
        return this.getValue().equals(other.getValue());
    }

    public abstract Collection<String> getValueSet(boolean includeNull);


}
