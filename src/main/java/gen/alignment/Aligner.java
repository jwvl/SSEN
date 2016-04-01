/**
 *
 */
package gen.alignment;

import forms.ElementCollection;
import forms.primitives.Subform;
import forms.primitives.Submapping;

import java.util.List;

/**
 * @author jwvl
 * @date May 9, 2015
 */
public interface Aligner<S extends Subform, T extends Subform, E extends ElementCollection<T>> {

    List<E> getGroupings(ElementCollection<S> top, ElementCollection<T> bottom, IAlignmentIndex index);

    List<Submapping<S, T>> getSubmappings(ElementCollection<S> top, ElementCollection<T> bottom, IAlignmentIndex index);

}
