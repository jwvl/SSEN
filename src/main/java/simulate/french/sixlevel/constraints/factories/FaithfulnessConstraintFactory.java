/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import forms.phon.PhoneTransform;
import gen.mapping.FormMapping;
import gen.mapping.specific.UfSfMapping;
import constraints.factories.PhoneTransformConstraintFactory;
import simulate.french.sixlevel.constraints.FaithfulnessConstraint;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public class FaithfulnessConstraintFactory extends
        PhoneTransformConstraintFactory {

    /* (non-Javadoc)
     * @see simulate.french.sixlevel.constraints.factories.SubformConstraintFactory#createConstraint(java.lang.Object)
     */
    @Override
    public FaithfulnessConstraint createConstraint(PhoneTransform offender) {
        return FaithfulnessConstraint.createInstance(offender);
    }


    /* (non-Javadoc)
     * @see constraints.factories.ConstraintFactory#addFormMapping(gen.mapping.FormMapping)
     */
    @Override
    public void addFormMapping(FormMapping fm) {
        if (fm instanceof UfSfMapping) {
            addTransgressor((UfSfMapping) fm);
        }

    }


}
