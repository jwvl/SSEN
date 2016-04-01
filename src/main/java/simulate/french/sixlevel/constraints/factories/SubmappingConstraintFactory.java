/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import forms.primitives.ISubmapping;
import gen.mapping.FormMapping;
import ranking.constraints.Constraint;
import ranking.constraints.factories.MappingConstraintFactory;
import ranking.constraints.helper.ConstraintArrayList;
import util.string.CollectionPrinter;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * @author jwvl
 * @date Aug 8, 2015
 */
public abstract class SubmappingConstraintFactory<F extends FormMapping, S extends ISubmapping<?, ?>> extends MappingConstraintFactory<F> {
    private Map<S, Constraint> constraintCache;


    protected SubmappingConstraintFactory(Map<S, Constraint> initialMap) {
        this.constraintCache = initialMap;
    }

    protected SubmappingConstraintFactory() {
        this.constraintCache = Maps.newHashMap();
    }

    /**
     * @param offender
     * @return
     */
    public abstract Constraint createConstraint(S offender);

    public abstract Collection<S> getOffenders(F submapping);

    @Override
    public void addTransgressor(F submapping) {
        Collection<S> offenders = getOffenders(submapping);
        for (S offender : offenders) {
            getConstraint(offender);
        }
    }

    @Override
    public List<Constraint> getConstraintsForMapping(F submapping) {
        List<Constraint> result = Lists.newArrayList();
        for (S offender : getOffenders(submapping)) {
            result.add(getConstraint(offender));
        }
        return result;
    }

    @Override
    protected void addViolatedForMapping(F formMapping, ConstraintArrayList list) {
        for (S offender : getOffenders(formMapping)) {
            list.add(getConstraint(offender));
        }
    }

    @Override
    public Collection<Constraint> getAll() {
        return constraintCache.values();
    }

    public Constraint getConstraint(S offender) {
        Constraint result = getConstraintCache().get(offender);
        if (result == null) {
            result = createConstraint(offender);
            System.out.println("Created constraint for " + offender);
            getConstraintCache().put(offender, result);
        }
        return result;
    }

    public void printConstraintMap() {
        CollectionPrinter.printMap(constraintCache);
    }

    public Map<S, Constraint> getConstraintCache() {
        return constraintCache;
    }


}
