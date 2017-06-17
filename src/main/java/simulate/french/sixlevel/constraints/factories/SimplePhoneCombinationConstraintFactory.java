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
            PhoneCombinationConstraint constraint = new PhoneCombinationConstraint(string);
            constraintMap.put(string,constraint);
        }
    }


    @Override
    public void addForm(PhoneticForm phones) {
        // does nothink
    }

    @Override
    public List<Constraint> getConstraintsForForm(PhoneticForm form) {
        return cache.get(form);
    }

    @Override
    protected void addViolatorsForForm(PhoneticForm right, ConstraintArrayList list) {
        list.addAll(cache.get(right));

    }
}

