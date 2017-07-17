/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.*;
import constraints.Constraint;
import constraints.factories.FormConstraintFactory;
import constraints.helper.ConstraintArrayList;
import forms.phon.flat.PhoneticForm;
import gen.mapping.FormMapping;
import simulate.french.sixlevel.constraints.PhoneCombinationConstraint;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public class SimplePhoneCombinationConstraintFactory extends FormConstraintFactory<PhoneticForm> {

    Map<String,PhoneCombinationConstraint> constraintMap;
    ListMultimap<PhoneticForm,Constraint> cache;


    public SimplePhoneCombinationConstraintFactory() {
        constraintMap = Maps.newHashMap();
        cache = ArrayListMultimap.create();
    }

    @Override
    public void addFormMapping(FormMapping fm) {
        addForm((PhoneticForm)(fm.right()));
    }

    @Override
    public Collection<Constraint> getAll() {
        return cache.values();
    }

    public void addFromString(String... input) {
        for (String string: input) {
            if (constraintMap.containsKey(string)) {

            } else {
                PhoneCombinationConstraint constraint = new PhoneCombinationConstraint(string);
                constraintMap.put(string,constraint);
            }

        }
    }


    @Override
    public void addForm(PhoneticForm phones) {

    }

    @Override
    public List<Constraint> getConstraintsForForm(PhoneticForm form) {
        return cache.get(form);
    }

    @Override
    protected void addViolatorsForForm(PhoneticForm right, ConstraintArrayList list) {
        for (String string: constraintMap.keySet()) {
            PhoneCombinationConstraint constraint = constraintMap.get(string);
            int numViolations = constraint.getNumViolations(right);
            list.addMultiple(constraint, numViolations);
        }
    }
}

