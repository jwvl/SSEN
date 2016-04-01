/**
 *
 */
package ranking.constraints.factories;

import gen.mapping.FormMapping;
import ranking.constraints.Constraint;
import ranking.constraints.helper.ConstraintArrayList;

import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public abstract class ConstraintFactory {

    public abstract void addFormMapping(FormMapping fm);

    public abstract Collection<Constraint> getAll();

    public abstract List<Constraint> getSpecificConstraints(FormMapping formMapping);

    public abstract void addViolatedToList(FormMapping formMapping, ConstraintArrayList list);


}
