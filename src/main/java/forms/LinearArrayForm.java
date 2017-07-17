package forms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import forms.primitives.Subform;

import java.util.*;

/**
 * A Form that is defined by an ordered collection of (generic) subforms.
 *
 * @param <S> Subform type
 * @author Jan-Willem van Leussen, Jan 15, 2015
 */
public abstract class LinearArrayForm<S extends Subform> implements LinearForm<S> {

    protected final S[] contents;

    public LinearArrayForm(S[] contents) {
        this.contents = contents;
    }

    public String[] getAsStrings() {
        String[] result = new String[contents.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = contents[i].toString();
        }
        return result;
    }

    @Override
    public int getNumSubForms() {
        return contents.length;
    }

    public int size() {
        return contents.length;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsList()
     */
    @Override
    public List<S> elementsAsList() {
        return ImmutableList.copyOf(contents);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsSet()
     */
    @Override
    public Set<S> elementsAsSet() {
        return ImmutableSet.copyOf(contents);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#recursiveSize()
     */
    @Override
    public int recursiveSize() {
        int countingSize = 0;
        for (Subform s : contents) {
            countingSize += s.size();
        }
        return countingSize;
    }

    @Override
    public int countSubform(Subform sf) {
        int result = 0;
        for (Subform s : contents) {
            if (s.equals(sf))
                result += 1;
        }
        return result;

    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#concatElementsToString(java.lang.String)
     */
    @Override
    public String concatElementsToString(String separator) {
        StringBuilder result = new StringBuilder();
        for (S s : contents) {
            result.append(s).append(separator);
        }
        return result.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform)
     */
    @Override
    public int getIndexOf(S element) {
        return getIndexOf(element, 0);
    }

    @Override
    public int getIndexOf(S subform, int startAt) {
        for (int i = startAt; i < size(); i++) {
            if (contents[i].equals(subform))
                return i;
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinearArrayForm<?> that = (LinearArrayForm<?>) o;
        return Arrays.deepEquals(contents, that.contents);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(contents);
    }

    /*
             * (non-Javadoc)
             *
             * @see java.lang.Iterable#iterator()
             */
    @Override
    public Iterator<S> iterator() {
        return Iterators.forArray(contents);
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsArray()
     */
    @Override
    public S[] elementsAsArray() {
        return contents;
    }

    @Override
    public int getLevelIndex() {
        return getLevel().myIndex();
    }

}
