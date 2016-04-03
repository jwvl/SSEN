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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date Dec 9, 2014
 */
public class Morpheme extends Subform implements ElementCollection<MElement> {

    private final ImmutableSortedSet<MElement> features;
    private final AttributeSet attributes;
    private final SyntacticCategory syntacticCategory;
    public final static Morpheme NULL_MORPHEME = new Morpheme();

    /*
     * Null constructor! Be careful!
     */
    private Morpheme() {
        syntacticCategory = null;
        features = ImmutableSortedSet.of();
        attributes = getAttributeSet(features);
    }

    /**
     * @return
     */
    private AttributeSet getAttributeSet(ImmutableSortedSet<MElement> features) {
        if (this.hasConceptFeature()) {
            return AttributeSet.STEM;
        }
        Set<String> attributes = Sets.newHashSet();
        for (MElement mElement : features) {
            attributes.add(mElement.getFeature().getAttribute());
        }
        return new AttributeSet(attributes);
    }

    private Morpheme(List<MElement> inputElements, SyntacticCategory syntacticCategory) {
        this.features = ImmutableSortedSet.copyOf(MElement.ORDERING, inputElements);
        attributes = getAttributeSet(features);
        this.syntacticCategory = syntacticCategory;
        // Assert input contained no duplicates
        assert (inputElements.size() == this.features.size());
    }

    public static Morpheme createInstance(List<MElement> inputs, SyntacticCategory syntacticCategory) {
        return new Morpheme(inputs, syntacticCategory);
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

        if (features != null ? !features.equals(mElements.features) : mElements.features != null) return false;
        if (attributes != null ? !attributes.equals(mElements.attributes) : mElements.attributes != null) return false;
        return syntacticCategory == mElements.syntacticCategory;

    }

    @Override
    public int hashCode() {
        int result = features != null ? features.hashCode() : 0;
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (syntacticCategory != null ? syntacticCategory.hashCode() : 0);
        return result;
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
     * @see forms.primitives.SubForm#size()
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
        List<MElement> listToSearch = elementsAsList();
        for (int i = startAt; i < listToSearch.size(); i++) {
            if (listToSearch.get(i).equals(subform)) {
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

}
