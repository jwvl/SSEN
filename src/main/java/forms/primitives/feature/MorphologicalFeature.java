/**
 *
 */
package forms.primitives.feature;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

/**
 * @author jwvl
 * @date May 25, 2015
 */
public class MorphologicalFeature extends AbstractMFeature {

    private static Table<String, String, MorphologicalFeature> instances = HashBasedTable.create();
    private static String NULL_CHAR = "∅";
    private String string;
    private final boolean isNull;

    /**
     * @param attribute
     * @param value
     */
    private MorphologicalFeature(String attribute, String value) {
        super(attribute, value);
        isNull = value.equals(NULL_CHAR);
    }

    public static MorphologicalFeature getNullInstance(String attribute) {
        return getInstance(attribute, NULL_CHAR);
    }

    public static MorphologicalFeature getInstance(String attribute, String value) {
        MorphologicalFeature feature = instances.get(attribute, value);
        if (feature == null) {
            feature = new MorphologicalFeature(attribute, value);
            instances.put(attribute, value, feature);
        }
        return feature;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.feature.Feature#isNull()
     */
    @Override
    public boolean isNull() {
        return isNull;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.feature.Feature#toString()
     */
    @Override
    public String toString() {
        if (string == null) {
            string = new StringBuilder(getAttribute()).append("[").append(getValue()).append("]").toString();
        }
        return string;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.feature.Feature#expressesValue()
     */
    @Override
    public boolean expressesValue() {
        return (!isNull());
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.feature.AbstractMFeature#getKnownValues()
     */
    @Override
    public Collection<String> getValueSet(boolean includeNull) {
        Set<String> result = instances.row(getAttribute()).keySet();
        if (!includeNull) {
            result.remove(String.valueOf(NULL_CHAR));
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.feature.Feature#createNullValue()
     */
    @Override
    public MorphologicalFeature createNullValue() {
        return getNullInstance(this.getAttribute());
    }

    public MorphologicalFeature getNullInstance() {
        return createNullValue();
    }

    public Collection<MorphologicalFeature> getExpressedSet() {
        Collection<MorphologicalFeature> result = instances.row(this.getAttribute()).values();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MorphologicalFeature that = (MorphologicalFeature) o;
        return isNull == that.isNull &&
                Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string, isNull);
    }
}
