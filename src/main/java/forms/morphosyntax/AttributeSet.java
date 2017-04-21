/**
 *
 */
package forms.morphosyntax;

import util.string.CollectionPrinter;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;

/**
 * @author jwvl
 * @date Jul 31, 2015
 */
public class AttributeSet {
    private final EnumSet<Attribute> attributes;
    public final static AttributeSet STEM = createStemAttribute();

    public AttributeSet(Collection<Attribute> attributes) {
        this.attributes = EnumSet.copyOf(attributes);
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
        return new AttributeSet(Collections.singleton(Attribute.CONCEPT));
    }

}
