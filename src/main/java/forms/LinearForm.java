/**
 *
 */
package forms;

import forms.primitives.Subform;

/**
 * @author jwvl
 * @date Aug 29, 2015
 */
public interface LinearForm<S extends Subform> extends Form, ElementCollection<S> {

    int size();

    @Override
    int getIndexOf(S subform, int startAt);
}
