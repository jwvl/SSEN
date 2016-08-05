/**
 *
 */
package constraints.factories;

import forms.Form;
import gen.mapping.FormMapping;
import constraints.Constraint;
import constraints.helper.ConstraintArrayList;

import java.util.List;

/**
 * @author jwvl
 * @date Aug 8, 2015
 */
public abstract class FormConstraintFactory<F extends Form> extends ConstraintFactory {


    public abstract void addForm(F f);

    @Override
    public final List<Constraint> getSpecificConstraints(FormMapping fm) {
        return getConstraintsForForm((F) fm.right());
    }

    public abstract List<Constraint> getConstraintsForForm(F form);

    @Override
    public final void addViolatedToList(FormMapping formMapping, ConstraintArrayList list) {
        addViolatorsForForm((F) formMapping.right(), list);
    }

    protected abstract void addViolatorsForForm(F right, ConstraintArrayList list);
}
