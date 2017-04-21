/**
 *
 */
package forms.primitives.feature;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

/**
 * @author jwvl
 * @date May 25, 2015 A Semantic Feature is a 'privative' feature of sorts,
 * whose attribute is always 'concept' value encoding its meaning.
 */
public class SemanticFeature extends AbstractMFeature {
    private static HashMap<String, SemanticFeature> cache = Maps.newHashMap();
    private final static String ATTRIBUTE_CONSTANT = "CONCEPT";
    private final static String NULL_VALUE = "0";
    private final static AbstractMFeature NULL_CONCEPT = new SemanticFeature(NULL_VALUE);

    /**
     * @param v
     */
    private SemanticFeature(String v) {
        super(ATTRIBUTE_CONSTANT, v);
    }

    public static SemanticFeature getInstance(String value) {
        SemanticFeature result = cache.get(value);
        if (result == null) {
            result = new SemanticFeature(value);
            cache.put(value, result);
        }
        return result;
    }



    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.feature.Feature#isNull()
     */
    @Override
    public boolean isNull() {
        return getValue().equals(NULL_VALUE);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.feature.Feature#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder("“").
                append(getValue()).
                append("”").
                toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.feature.Feature#expressesValue()
     */
    @Override
    public boolean expressesValue() {
        return !isNull();
    }


    public Collection<String> getValueSet(boolean includeNull) {
        Set<String> result = cache.keySet();
        if (includeNull)
            result.add(NULL_VALUE);
        return result;
    }

    /* (non-Javadoc)
     * @see forms.primitives.feature.Feature#getNullValue(java.lang.String)
     */
    @Override
    public Feature<String> createNullValue() {
        return NULL_CONCEPT;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SemanticFeature))
            return false;
        SemanticFeature other = (SemanticFeature) obj;
        if (getValue() == null) {
            if (other.getValue() != null)
                return false;
        } else if (!getValue().equals(other.getValue()))
            return false;
        return true;
    }



}
