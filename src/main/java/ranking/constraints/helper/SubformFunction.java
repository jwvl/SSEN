/**
 *
 */
package ranking.constraints.helper;

import com.google.common.base.Function;
import forms.primitives.Subform;

/**
 * A template 'helper function' for Constraints which punishes certain Subforms.
 *
 * @author jwvl
 * @date May 23, 2015
 */
public interface SubformFunction<S extends Subform> extends Function<S, Integer> {

}
