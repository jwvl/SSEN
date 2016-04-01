package forms;

import com.google.common.collect.ImmutableList;
import forms.primitives.Subform;

import java.util.*;

/**
 * A Form that is defined by an ordered collection of (generic) subforms.
 *
 * @param <S> Subform type
 * @author Jan-Willem van Leussen, Jan 15, 2015
 */
public abstract class LinearListForm<S extends Subform> implements LinearForm<S> {
    final protected ImmutableList<S> contents;

    public LinearListForm(Collection<S> contents) {
        super();
        this.contents = ImmutableList.copyOf(contents);
    }

    public String[] getAsStrings() {
        String[] result = new String[contents.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = contents.get(i).toString();
        }
        return result;
    }

    @Override
    public int getNumSubForms() {
        return contents.size();
    }

    public int size() {
        return contents.size();
    }

    public String spellOut() {
        StringBuffer result = new StringBuffer();
        for (Subform s : contents) {
            if (!s.isNull())
                result.append(s.toString());
        }
        return result.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsList()
     */
    @Override
    public List<S> elementsAsList() {
        return contents;
    }

    /*
     * (non-Javadoc)
     *
     * @see forms.ElementCollection#elementsAsSet()
     */
    @Override
    public Set<S> elementsAsSet() {
        return new HashSet<S>(contents);
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
    public Iterator<S> iterator() {
        return contents.iterator();
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
        return contents.indexOf(element);
    }

    @Override
    public int getIndexOf(S subform, int startAt) {
        for (int i = startAt; i < size(); i++) {
            if (contents.get(i).equals(subform))
                return i;
        }
        return -1;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((contents == null) ? 0 : contents.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof LinearListForm))
            return false;
        LinearListForm other = (LinearListForm) obj;
        if (contents == null) {
            if (other.contents != null)
                return false;
        } else if (!contents.equals(other.contents))
            return false;
        return true;
    }

    @Override
    public int getLevelIndex() {
        return getLevel().myIndex();
    }
}
