/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.primitives.segment.Phone;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;
import constraints.SubformCountingConstraint;

/**
 * @author jwvl
 * @date Aug 1, 2015
 */
public class PhoneArticulatoryConstraint extends SubformCountingConstraint<Phone> {

    /**
     * @param rightLevel
     * @param subform
     */
    private PhoneArticulatoryConstraint(Level rightLevel, Phone subform) {
        super(rightLevel, subform);
    }

    public PhoneArticulatoryConstraint(Phone phone) {
        super(BiPhonSix.getPhoneticLevel(), phone);
    }

}
