/**
 *
 */
package forms;

import forms.primitives.Subform;

import java.util.List;
import java.util.Set;

/**
 * @author Jan-Willem van Leussen, Dec 23, 2014
 */
public interface ElementCollection<S extends Subform> extends Iterable<S> {

    S[] elementsAsArray();

    List<S> elementsAsList();

    Set<S> elementsAsSet();

    int size();

    int recursiveSize();

    int getIndexOf(S subform);

    int getIndexOf(S subform, int startAt);

    /**
     * Returns a string with all elements in the ElementColleciton concatenated,
     * separated by the supplied separator.
     *
     * @param separator String/character to separate elements
     * @return a string with all elements in the ElementColleciton concatenated
     */
    String concatElementsToString(String separator);

}
