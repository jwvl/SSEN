/**
 *
 */
package forms.morphosyntax;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import forms.ElementCollection;
import forms.primitives.Subform;
import util.string.CollectionPrinter;

import java.util.*;

/**
 * @author jwvl
 * @date Dec 9, 2014
 */
public class Morpheme implements Subform, ElementCollection<MElement> {

    private final ImmutableSortedSet<MElement> features;
    private final AttributeSet attributes;
    private final SyntacticCategory syntacticCategory;
    private final int hashcode;
    public final static Morpheme NULL_MORPHEME = new Morpheme();

    /*
     * Null constructor! Be careful!
     */
    private Morpheme() {
        syntacticCategory = null;
        features = ImmutableSortedSet.of();
        attributes = getAttributeSet(features);
        hashcode = Objects.hash(features, syntacticCategory);
    }

    /**
     * @return
     */
    private AttributeSet getAttributeSet(ImmutableSortedSet<MElement> features) {
        Set<Attribute> attributes = Sets.newHashSet();
        for (MElement mElement : features) {
            attributes.add(mElement.getFeature().attribute);
        }
        return new AttributeSet(attributes);
    }

    private Morpheme(List<MElement> inputElements, SyntacticCategory syntacticCategory) {
        this.features = ImmutableSortedSet.copyOf(MElement.ORDERING, inputElements);
        attributes = getAttributeSet(features);
        this.syntacticCategory = syntacticCategory;
        // Assert input contained no duplicates
        assert (inputElements.size() == this.features.size());
        hashcode = Objects.hash(features, syntacticCategory);
    }

    public static Morpheme createInstance(List<MElement> inputs, SyntacticCategory syntacticCategory) {
        Morpheme result = new Morpheme(inputs, syntacticCategory);
        return result;
    }

    public static Morpheme createInstance(Set<MElement> inputs, SyntacticCategory syntacticCategory) {
        List<MElement> elementsSorted = Lists.newArrayList(inputs);
        Collections.sort(elementsSorted, MElement.ORDERING);
        return createInstance(elementsSorted, syntacticCategory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Morpheme mElements = (Morpheme) o;
        return syntacticCategory == mElements.syntacticCategory &&
                features.equals(((Morpheme) o).features);
    }

    @Override
    public int hashCode() {
        return hashcode;
    }

    /*
                     * (non-Javadoc)
                     *
                     * @see forms.primitives.SubForm#isNullElement()
                     */
    @Override
    public boolean isNull() {
        return this.equals(NULL_MORPHEME);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.SubForm#getNumSteps()
     */
    @Override
    public int size() {
        return features.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.primitives.SubForm#toString()
     */
    @Override
    public String toString() {
        if (this.isNull()) {
            return "∅";
        } else {
            return CollectionPrinter.collectionToString(features, ".") + "_" + syntacticCategory;
        }
    }

    public Morpheme fromString(String input) {
        if (input.equals("∅")) {
            return Morpheme.NULL_MORPHEME;
        } else {
            int underScoreIndex = input.indexOf("_");
            String synCatString = input.substring(underScoreIndex);
            SyntacticCategory syncat = SyntacticCategory.getInstance(synCatString);
            String morphsString = input.substring(0,underScoreIndex);
            String[] morphs = morphsString.split(".");
            List<MElement> mElements = Lists.newArrayList();
            for (String morph: morphs) {
               // mElements.add(Me)
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<MElement> iterator() {
        return features.iterator();
    }

    public MElement getElementAt(int index) {
        return features.asList().get(index);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsList()
     */
    @Override
    public List<MElement> elementsAsList() {
        return features.asList();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsSet()
     */
    @Override
    public Set<MElement> elementsAsSet() {
        return features;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#recursiveSize()
     */
    @Override
    public int recursiveSize() {
        return size();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#concatElementsToString(java.lang.String)
     */
    @Override
    public String concatElementsToString(String separator) {
        StringBuilder result = new StringBuilder();
        for (MElement me : features) {
            result.append(me).append(separator);
        }
        return result.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform)
     */
    @Override
    public int getIndexOf(MElement element) {
        return elementsAsList().indexOf(element);
    }

    public boolean hasConceptFeature() {
        for (MElement mElement : this) {
            if (mElement.isConcept())
                return true;
        }
        return false;
    }

    public AttributeSet getAttributes() {
        return attributes;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform, int)
     */
    @Override
    public int getIndexOf(MElement subform, int startAt) {
        MElement[] listToSearch =elementsAsArray();;
        for (int i = startAt; i < listToSearch.length; i++) {
            if (listToSearch[i].equals(subform)) {
                return i;
            }
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#elementsAsArray()
     */
    @Override
    public MElement[] elementsAsArray() {
        return features.toArray(new MElement[size()]);
    }

    public boolean containsAffixType(AffixType type) {
        if (type.getSyntacticCategory() == syntacticCategory) {
             for (MElement mElement: features) {
                 if (mElement.getFeature().attribute.equals(type.getAttribute())) {
                     return true;
                 }
             }
        }
        return false;
    }

    public AffixType[] getAffixTypes() {
        SyntacticCategory syntacticCategory = this.syntacticCategory;
        AffixType[] result = new AffixType[this.size()];
        int count = 0;
        for (MElement mElement: this) {
            result[count++] = AffixType.createInstance(syntacticCategory,mElement.getFeature().attribute);
        }
        return result;
    }

    public SyntacticCategory getSyntacticCategory() {
        return syntacticCategory;
    }
}
