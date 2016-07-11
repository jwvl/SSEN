/**
 *
 */
package forms.phon.numerical;

import com.google.common.collect.ImmutableSortedSet;
import forms.ElementCollection;
import forms.primitives.Subform;
import util.string.StringPrinter;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date 03/12/2015
 */
public class PhoneticSignal implements Subform, ElementCollection<PhoneticElement> {

    private final ImmutableSortedSet<PhoneticElement> elements;


    /**
     * @param elements
     */
    private PhoneticSignal(Iterable<PhoneticElement> elements) {
        this.elements = ImmutableSortedSet.copyOf(elements);
    }

    public static PhoneticSignal createInstance(List<PhoneticElement> elements) {
        return new PhoneticSignal(elements);
    }

    /* (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<PhoneticElement> iterator() {
        return elements.iterator();
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#elementsAsArray()
     */
    @Override
    public PhoneticElement[] elementsAsArray() {
        return elements.toArray(new PhoneticElement[size()]);
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#elementsAsList()
     */
    @Override
    public List<PhoneticElement> elementsAsList() {
        return elements.asList();
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#elementsAsSet()
     */
    @Override
    public Set<PhoneticElement> elementsAsSet() {
        return elements;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#recursiveSize()
     */
    @Override
    public int recursiveSize() {
        return size();
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform)
     */
    @Override
    public int getIndexOf(PhoneticElement subform) {
        return getIndexOf(subform, 0);
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#getIndexOf(forms.primitives.Subform, int)
     */
    @Override
    public int getIndexOf(PhoneticElement subform, int startAt) {
        int i = startAt;
        for (PhoneticElement element : elements) {
            if (element.equals(subform)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /* (non-Javadoc)
     * @see forms.ElementCollection#concatElementsToString(java.lang.String)
     */
    @Override
    public String concatElementsToString(String separator) {
        return StringPrinter.linearizeList(this.elementsAsList(), separator);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((elements == null) ? 0 : elements.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof PhoneticSignal))
            return false;
        PhoneticSignal other = (PhoneticSignal) obj;
        if (elements == null) {
            if (other.elements != null)
                return false;
        } else if (!elements.equals(other.elements))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#isNull()
     */
    @Override
    public boolean isNull() {
        return elements.isEmpty();
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#getNumSteps()
     */
    @Override
    public int size() {
        return elements.size();
    }

    /* (non-Javadoc)
     * @see forms.primitives.Subform#toString()
     */
    @Override
    public String toString() {
        return concatElementsToString(", ");
    }

}
