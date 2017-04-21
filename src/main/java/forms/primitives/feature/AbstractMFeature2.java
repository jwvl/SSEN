/**
 *
 */
package forms.primitives.feature;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import forms.morphosyntax.MFeatureAttribute;

import java.util.Objects;

/**
 * @author jwvl
 * @date May 25, 2015
 */
public class AbstractMFeature2 {
    private static Table<MFeatureAttribute, String, AbstractMFeature2> cache = HashBasedTable.create();
    private final static String NULL_VALUE = "0";
    private final static AbstractMFeature2 NULL_INSTANCE = getSemanticFeature(NULL_VALUE);
    private final MFeatureAttribute attribute;
    private final String value;


    public static AbstractMFeature2 getSemanticFeature(String value) {
        return getFeature(MFeatureAttribute.CONCEPT,value);
    }

    public static AbstractMFeature2 getMorphologicalFeature(String attribute, String value) {
        return getFeature(MFeatureAttribute.valueOf(attribute),value);
    }

    public static AbstractMFeature2 getNullInstance(String attribute) {
        return getFeature(MFeatureAttribute.valueOf(attribute), NULL_VALUE);
    }


    private static AbstractMFeature2 getFeature(MFeatureAttribute attribute, String value) {
        AbstractMFeature2 result =(cache.get(attribute,value));
        if (result == null) {
            result = new AbstractMFeature2(attribute,value);
            cache.put(attribute, value,result);
        }
        return result;
    }

    public AbstractMFeature2(MFeatureAttribute attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public boolean isConcept() {
        return attribute == MFeatureAttribute.CONCEPT;
    }

    public boolean valueEquals(AbstractMFeature2 other) {
        return value.equals(other.value);
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
        return Objects.hash(attribute, value);
    }
}
