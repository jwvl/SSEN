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
 * @date May 24, 2015
 */
public interface Alignment<S extends Subform, T extends Subform> {

    List<Submapping<S, T>> getDownMappings(ElementCollection<S> sCol, ElementCollection<T> tCol);

    List<Submapping<T, S>> getUpMappings(ElementCollection<S> sCol, ElementCollection<T> tCol);
}
