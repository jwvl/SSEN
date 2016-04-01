/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.primitives.segment.Phone;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;
import ranking.constraints.SubformCountingConstraint;

/**
 * @author jwvl
 * @date Aug 1, 2015
 */
public class ArticulatoryConstraint extends SubformCountingConstraint<Phone> {

    /**
     * @param rightLevel
     * @param subform
     */
    private ArticulatoryConstraint(Level rightLevel, Phone subform) {
        super(rightLevel, subform);
    }

    public ArticulatoryConstraint(Phone phone) {
        super(BiPhonSix.getPhoneticLevel(), phone);
    }

}
