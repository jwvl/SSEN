/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import forms.Form;
import gen.mapping.FormMapping;
import ranking.constraints.Constraint;
import ranking.constraints.factories.FormConstraintFactory;
import ranking.constraints.helper.ConstraintArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jwvl
 * @date Aug 1, 2015
 */
public abstract class SubformConstraintFactory<F extends Form, O extends Object>
        extends FormConstraintFactory<F> {

    @SuppressWarnings("unchecked")
    @Override
    public void addFormMapping(FormMapping fm) {
        addForm((F) fm.right());
    }

    private Map<O, Constraint> constraintCache;


    /**
     * @param constraintCache
     */
    protected SubformConstraintFactory() {
        this.constraintCache = Maps.newConcurrentMap();
    }

    /**
     * @param offender
     * @return
     */
    public abstract Constraint createConstraint(O offender);

    public abstract Collection<O> findOffenders(F form);

    @Override
    public void addForm(F form) {
        Collection<O> offenders = findOffenders(form);
        for (O offender : offenders) {
            getConstraint(offender);
        }
    }

    @Override
    public List<Constraint> getConstraintsForForm(F form) {
        List<Constraint> result = Lists.newArrayList();
        for (O offender : findOffenders(form)) {
            result.add(getConstraint(offender));
        }
        return result;
    }

    @Override
    public Collection<Constraint> getAll() {
        return constraintCache.values();
    }

    public Constraint getConstraint(O offender) {
        Constraint result = constraintCache.get(offender);
        if (result == null) {
            result = createConstraint(offender);
            System.out.println("Created constraint: " + result);
            constraintCache.put(offender, result);
        }
        return result;
    }

    @Override
    protected void addViolatorsForForm(F form, ConstraintArrayList list) {
        for (O offender : findOffenders(form)) {
            list.add(getConstraint(offender));
        }
    }
}
