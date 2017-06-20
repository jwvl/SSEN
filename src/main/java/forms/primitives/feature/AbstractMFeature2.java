/**
 *
 */
package forms.primitives.feature;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import forms.morphosyntax.Attribute;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * @author jwvl
 * @date May 25, 2015
 */
public class AbstractMFeature2 {
    private static Table<Attribute, String, AbstractMFeature2> cache = HashBasedTable.create();
    private static String NULL_CHAR = "∅";
    private final static AbstractMFeature2 NULL_INSTANCE = getSemanticFeature(NULL_CHAR);
    public final Attribute attribute;
    public final String value;
    public final String string;
    private final int hashCode;


    public static AbstractMFeature2 getSemanticFeature(String value) {
        return getInstance(Attribute.CONCEPT,value);
    }

    public static AbstractMFeature2 getInstance(String attribute, String value) {
        return getInstance(Attribute.valueOf(attribute.toUpperCase()), value);
    }

    public static AbstractMFeature2 getMorphologicalFeature(String attribute, String value) {
        return getInstance(Attribute.valueOf(attribute.toUpperCase()),value);
    }

    public static AbstractMFeature2 getNullInstance(String attribute) {
        return getInstance(Attribute.valueOf(attribute.toUpperCase()), NULL_CHAR);
    }

    public static AbstractMFeature2 getNullInstance(Attribute attribute) {
        return getInstance(attribute, NULL_CHAR);
    }


    private static AbstractMFeature2 getInstance(Attribute attribute, String value) {
        AbstractMFeature2 result =(cache.get(attribute,value));
        if (result == null) {
            result = new AbstractMFeature2(attribute,value);
            cache.put(attribute, value,result);
        }
        return result;
    }

    private AbstractMFeature2(Attribute attribute, String value) {
        this.attribute = attribute;
        this.value = value;
        hashCode = Objects.hash(attribute, value);
        if (attribute == Attribute.CONCEPT) {
            string = new StringBuilder("“").
                    append(value).
                    append("”").
                    toString();
        } else {
            string = new StringBuilder(attribute.toString()).append("[").append(value).append("]").toString();
        }

    }

    public boolean isConcept() {
        return attribute == Attribute.CONCEPT;
    }

    public boolean isNull() {
        return value.equals(NULL_CHAR);
    }

    public boolean valueEquals(AbstractMFeature2 other) {
        return value.equals(other.value);
    }

    public Collection<String> getValueSet(boolean includeNull) {
        Set<String> result = cache.row(attribute).keySet();
        if (!includeNull) {
            result.remove(NULL_CHAR);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMFeature2 that = (AbstractMFeature2) o;
        return Objects.equals(attribute, that.attribute) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public Collection<AbstractMFeature2> getExpressedSet(boolean includeNull) {
        Collection<AbstractMFeature2> fromCache = cache.row(attribute).values();
        Set<AbstractMFeature2> result = Sets.newHashSet(fromCache);
        if (!includeNull) {
            result.remove(getNullInstance(attribute));
        }
        return result;
    }

    @Override
    public String toString() {
        return string;
    }
}
