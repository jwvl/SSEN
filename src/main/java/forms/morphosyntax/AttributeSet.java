/**
 *
 */
package forms.morphosyntax;

import com.google.common.collect.ImmutableSet;
import util.string.CollectionPrinter;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeSet that = (AttributeSet) o;
        return Objects.equals(attributes, that.attributes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attributes);
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
