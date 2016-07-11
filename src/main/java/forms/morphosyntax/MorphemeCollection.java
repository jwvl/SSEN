package forms.morphosyntax;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import forms.ElementCollection;
import util.string.CollectionPrinter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date Aug 29, 2015
 */
public class MorphemeCollection implements ElementCollection<Morpheme> {

    private final Morpheme[] contents;

    public MorphemeCollection(Morpheme[] contents) {
        this.contents = contents;
    }

    public MorphemeCollection(List<Morpheme> contents) {
        this.contents = contents.toArray(new Morpheme[contents.size()]);
    }

    @Override
    public Iterator<Morpheme> iterator() {
        return Iterators.forArray(contents);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsArray()
     */
    @Override
    public Morpheme[] elementsAsArray() {
        return contents;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsList()
     */
    @Override
    public List<Morpheme> elementsAsList() {
        // TODO Auto-generated method stub
        return ImmutableList.copyOf(contents);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsSet()
     */
    @Override
    public Set<Morpheme> elementsAsSet() {
        return ImmutableSet.copyOf(contents);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#getNumSteps()
     */
    @Override
    public int size() {
        // TODO Auto-generated method stub
        return contents.length;
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
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform)
     */
    @Override
    public int getIndexOf(Morpheme subform) {
        return getIndexOf(subform, 0);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform, int)
     */
    @Override
    public int getIndexOf(Morpheme subform, int startAt) {
        for (int i = startAt; i < contents.length; i++) {
            if (contents[i] == subform) {
                return i;
            }
        }
        return -1;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#concatElementsToString(java.lang.String)
     */
    @Override
    public String concatElementsToString(String separator) {
        return CollectionPrinter.collectionToString(elementsAsList(), separator);
    }

}
