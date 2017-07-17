/**
 *
 */
package forms.morphosyntax;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import forms.ElementCollection;
import forms.primitives.Subform;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author jwvl
 * @date Jul 31, 2015
 */
public class MorphologicalWord implements Subform, ElementCollection<Morpheme> {

    private final SyntacticCategory category;
    private final List<Morpheme> morphemes;
    private final int hashCode;

    /**
     * @param category
     * @param morphemes
     */
    public MorphologicalWord(SyntacticCategory category,
                             List<Morpheme> morphemes) {
        this.category = category;
        this.morphemes = ImmutableList.copyOf(morphemes);
        this.hashCode = calculateHashCode();
    }

	/* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
	 */

    @Override
    public Iterator<Morpheme> iterator() {
        return morphemes.iterator();
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#elementsAsList()
     */
    @Override
    public List<Morpheme> elementsAsList() {
        return morphemes;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#elementsAsSet()
     */
    @Override
    public Set<Morpheme> elementsAsSet() {
        return Sets.newHashSet(morphemes);
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#recursiveSize()
     */
    @Override
    public int recursiveSize() {
        int result = 0;
        for (Morpheme m : this) {
            result += m.size();
        }
        return result;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#concatElementsToString(java.lang.String)
     */
    @Override
    public String concatElementsToString(String separator) {
        if (size() == 0)
            return "";
        StringBuilder result = new StringBuilder(morphemes.get(0).toString());
        for (int i = 1; i < size(); i++) {
            result.append(separator).append(morphemes.get(i));
        }
        return result.toString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MorphologicalWord morphemes1 = (MorphologicalWord) o;

        if (category != morphemes1.category) return false;
        return morphemes != null ? morphemes.equals(morphemes1.morphemes) : morphemes1.morphemes == null;

    }
    @Override
    public int hashCode() {
        return hashCode;
    }


    public int calculateHashCode() {
        return Objects.hash(category, morphemes);
    }

    /* (non-Javadoc)
             * @see forms.primitives.Subform#isNull()
             */
    @Override
    public boolean isNull() {
        return size() < 1;
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#getNumSteps()
     */
    @Override
    public int size() {
        return morphemes.size();
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#toString()
     */
    @Override
    public String toString() {
        return concatElementsToString("+");
    }

    public MorphologicalWord readFromString(String input) {
        String[] split = input.split("\\+");
        for (String part: split) {
        //    Morpheme morpheme = new Morpheme(§
        }
        return null;
    }

    public int getConceptMorphemeIndex() {
        for (int i = 0; i < size(); i++) {
            if (morphemes.get(i).hasConceptFeature())
                return i;
        }
        return -1;
    }

    /**
     * @return the category
     */
    public SyntacticCategory getCategory() {
        return category;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform, int)
     */
    @Override
    public int getIndexOf(Morpheme subform, int startAt) {
        for (int i = startAt; i < size(); i++) {
            if (morphemes.get(i).equals(subform))
                return i;
        }
        return -1;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform)
     */
    @Override
    public int getIndexOf(Morpheme element) {
        return getIndexOf(element, 0);
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#elementsAsArray()
     */
    @Override
    public Morpheme[] elementsAsArray() {
        return morphemes.toArray(new Morpheme[size()]);
    }
}
