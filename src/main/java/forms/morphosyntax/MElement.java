/**
 *
 */
package forms.morphosyntax;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Ordering;
import com.google.common.collect.Table;
import forms.primitives.Subform;
import forms.primitives.feature.AbstractMFeature2;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Wrapper for an MFeature so it can function as a Subform in a Morpheme.
 *
 * @author jwvl
 * @date Dec 24, 2014
 */
public class MElement implements Subform, Comparable<MElement> {
    private static int SERIAL_COUNTER = 0;
    private static Table<AbstractMFeature2, MFeatureType, MElement> cache = HashBasedTable.create();
    private final int serialNumber;
    private final AbstractMFeature2 feature;
    private final MFeatureType type;
    private final int hashcode;

    public int getSerialNumber() {
        return serialNumber;
    }

    /**
     * Private constructor.
     *
     * @param f Feature to wrap around
     */
    private MElement(AbstractMFeature2 f, MFeatureType type) {
        this.feature = f;
        this.serialNumber = SERIAL_COUNTER++;
        this.type = type;
        this.hashcode = Objects.hash(feature, type);
    }

    /**
     * Static constructor
     *
     * @param f Feature to wrap around
     * @return
     */
    public static MElement getInstance(AbstractMFeature2 f, MFeatureType t) {
        MElement result = cache.get(f, t);
        if (result == null) {
            result = new MElement(f, t);
            cache.put(f, t, result);
            System.out.println("Created new MElement: " + f + ", " + t.name());
            if (f.isNull() && t == MFeatureType.FIXED) {
                System.exit(0);
            }
        }
        return result;
    }

    /**
     * Ordering for M-elements, based on MFeature's ordering.
     */
    public static Ordering<MElement> ORDERING = new Ordering<MElement>() {

        @Override
        public int compare(MElement arg0, MElement arg1) {
            int enumSort = arg0.type.compareTo(arg1.type);
            if (enumSort != 0)
                return enumSort;
            else
                return arg0.toString().compareTo(arg1.toString());
        }

    };

    /**
     * @return true if this MElement represents a feature.
     */
    public boolean isConcept() {
        return feature.isConcept();
    }

    /**
     * Returns the feature this MElement wraps around.
     *
     * @return the feature
     */
    public AbstractMFeature2 getFeature() {
        return feature;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.Subform#isNull()
     */
    @Override
    public boolean isNull() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.Subform#getNumSteps()
     */
    @Override
    public int size() {
        return 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.Subform#toString()
     */
    @Override
    public String toString() {
        return feature.toString();
    }

    public MElement readFromString(String input, MFeatureType type) {
        return new MElement(AbstractMFeature2.readfromString(input),type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MElement mElement = (MElement) o;
        return Objects.equals(feature, mElement.feature) &&
                type == mElement.type;
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    public boolean attributeEquals(MElement other) {
        return (feature.attribute == other.feature.attribute);
    }

    /**
     * Create a Concept type feature
     *
     * @param concept String representing the concept
     * @return A newly created MFeature
     */
    public static MElement createConcept(String concept) {
        System.out.println("Attempting to create Concept from string " + concept);
        AbstractMFeature2 sf = AbstractMFeature2.getSemanticFeature(concept);
        return getInstance(sf, MFeatureType.CONCEPT);
    }

    /**
     * Create a Fixed type M-feature
     *
     * @param attribute Attribute of Feature to create
     * @param value     Value of feature to create
     * @return A new `fixed' type M-feature
     */
    public static MElement createFromStrings(String attribute, String value,
                                             MFeatureType type) {
        AbstractMFeature2 mf = AbstractMFeature2.getInstance(attribute,
                value);
        return getInstance(mf, type);
    }

    /**
     * @return A copy of this MElement with feature type CONTEXTUAL.
     */
    public MElement contextualCopy() {
        return getInstance(feature, MFeatureType.CONTEXTUAL);
    }

    public MFeatureType getType() {
        return type;
    }

    /**
     * @return
     */
    public boolean expressesValue() {
        return !feature.isNull();
    }

    public String getFeatureValue() {
        return feature.value;
    }

    /**
     * @return
     */
    public Set<MElement> getRealizationSet() {
        boolean removeNulls = (type == MFeatureType.FIXED);
        Collection<AbstractMFeature2> asFeatures = feature.getExpressedSet(removeNulls);
        Set<MElement> result = new HashSet<MElement>(asFeatures.size());
        for (AbstractMFeature2 mf : asFeatures) {
            result.add(getInstance(mf, this.type));
        }
        return result;
    }

    @Override
    public int compareTo(MElement o) {
        int result = type.ordinal() - o.type.ordinal();
        if (result == 0)
            result = feature.toString().compareTo(o.feature.toString());
        return result;
    }

    public Attribute getAttribute() {
        return feature.attribute;
    }


}
