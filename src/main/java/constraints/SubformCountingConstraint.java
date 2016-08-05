/**
 *
 */
package constraints;

import forms.LinearForm;
import forms.primitives.Subform;
import grammar.levels.Level;

/**
 * @author jwvl
 * @date Jul 28, 2015
 */
public abstract class SubformCountingConstraint<S extends Subform> extends FormConstraint<LinearForm<S>> {

    private S offendingSubform;

    /**
     * @param rightLevel
     */
    protected SubformCountingConstraint(Level rightLevel, S subform) {
        super(rightLevel);
        this.offendingSubform = subform;
    }

    @Override
    public int getNumViolations(LinearForm<S> f) {
        int result = 0;
        for (S s : f) {
            if (s.equals(offendingSubform))
                result++;
        }
        return result;
    }

    @Override
    public String toString() {
        return "*" + offendingSubform.toString();
    }

    @Override
    public boolean caches() {
        return false;
    }


}
