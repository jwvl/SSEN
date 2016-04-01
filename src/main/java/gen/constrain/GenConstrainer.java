/**
 *
 */
package gen.constrain;

import forms.Form;

/**
 * @author jwvl
 * @date Jul 31, 2015
 */
public interface GenConstrainer<F extends Form> {
    boolean canGenerate(F form);

}
