/**
 *
 */
package constraints.factories;

import gen.mapping.FormMapping;
import constraints.Constraint;
import constraints.helper.ConstraintArrayList;

import java.util.List;

/**
 * @author jwvl
 * @date Aug 8, 2015
 */
public abstract class MappingConstraintFactory<F extends FormMapping> extends ConstraintFactory {


    public abstract void addTransgressor(F f);

    @Override
    public final List<Constraint> getSpecificConstraints(FormMapping fm) {
        return getConstraintsForMapping((F) fm);
    }

    @Override
    public void addViolatedToList(FormMapping formMapping, ConstraintArrayList list) {
        addViolatedForMapping((F) formMapping, list);
    }

    protected abstract void addViolatedForMapping(F formMapping, ConstraintArrayList list);

    public abstract List<Constraint> getConstraintsForMapping(F transgressor);

}
