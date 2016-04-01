/**
 *
 */
package forms.morphosyntax;

import com.google.common.collect.ImmutableSet;
import util.string.CollectionPrinter;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author jwvl
 * @date Jul 31, 2015
 */
public class AttributeSet {
    private final Set<String> attributes;
    public final static AttributeSet STEM = createStemAttribute();

    public AttributeSet(Collection<String> attributes) {
        this.attributes = ImmutableSet.copyOf(attributes);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((attributes == null) ? 0 : attributes.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AttributeSet))
            return false;
        AttributeSet other = (AttributeSet) obj;
        if (attributes == null) {
            if (other.attributes != null)
                return false;
        } else if (!attributes.equals(other.attributes))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return CollectionPrinter.collectionToString(attributes, ".");
    }

    /**
     * @return
     */
    private static AttributeSet createStemAttribute() {
        String attribute = "Stem";
        return new AttributeSet(Collections.singleton(attribute));
    }

}
