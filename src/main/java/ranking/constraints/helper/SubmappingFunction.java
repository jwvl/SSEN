/**
 *
 */
package ranking.constraints.helper;

import com.google.common.base.Function;
import forms.primitives.Submapping;

/**
 * A template 'helper function' for Constraints which assign violations to certain
 * submappings.
 *
 * @author jwvl
 * @date May 24, 2015
 */
public interface SubmappingFunction<S extends Submapping<?, ?>> extends Function<S, Integer> {


}
