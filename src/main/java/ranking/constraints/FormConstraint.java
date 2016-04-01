/**
 *
 */
package ranking.constraints;

import forms.Form;
import gen.mapping.FormMapping;
import grammar.levels.Level;

/**
 * Abstract subclass of Constraint that is restricted to inflicting violations
 * on Form types.
 *
 * @author jwvl
 * @date Nov 16, 2014
 */
public abstract class FormConstraint<F extends Form> extends Constraint {

    protected FormConstraint(Level rightLevel) {
        super(rightLevel);
    }

    public abstract int getNumViolations(F f);

    @Override
    public int getNumViolations(FormMapping t) {
        int result;
        if (!(t.getLevel().equals(rightLevel)))
            result = 0;
        else {
            result = getNumViolations((F) t.right());
        }
        return result;
    }
}
