/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import forms.phon.flat.PhoneticForm;
import forms.primitives.segment.Phone;
import simulate.french.sixlevel.constraints.PhoneArticulatoryConstraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date Aug 1, 2015
 * NB: This class
 */
public class ArticulatoryConstraintFactory extends
        SubformConstraintFactory<PhoneticForm, Phone> {
    private final Phone[] forbiddenPhones;

    public ArticulatoryConstraintFactory(Phone[] forbiddenPhones) {
        super();
        this.forbiddenPhones = forbiddenPhones;
    }


    public static ArticulatoryConstraintFactory createFromPhones(Phone... forbidden) {
        return new ArticulatoryConstraintFactory(forbidden);
    }

    public static ArticulatoryConstraintFactory createFromPhones(List<Phone> forbidden) {
        Phone[] asArray = new Phone[forbidden.size()];
        return new ArticulatoryConstraintFactory(forbidden.toArray(asArray));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.SubformConstraintFactory
     * #createConstraint(java.lang.Object)
     */
    @Override
    public PhoneArticulatoryConstraint createConstraint(Phone offender) {
        return new PhoneArticulatoryConstraint(offender);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.SubformConstraintFactory
     * #getOffenders(graph.Transgressor)
     */
    @Override
    public Collection<Phone> findOffenders(PhoneticForm transgressor) {
        List<Phone> offenders = new ArrayList<Phone>(transgressor.size());
        for (Phone p : transgressor.elementsAsArray()) {
            if (isForbidden(p)) {
                offenders.add(p);
            }
        }
        return offenders;
    }

    private boolean isForbidden(Phone p) {
        for (Phone f : forbiddenPhones) {
            if (f.equals(p)) {
                return true;
            }
        }
        return false;
    }

}
