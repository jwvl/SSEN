/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import constraints.factories.PhoneTransformConstraintFactory;
import forms.phon.PhoneTransform;
import gen.mapping.FormMapping;
import gen.mapping.specific.SfPfMapping;
import simulate.french.sixlevel.constraints.CueConstraint;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public class CueConstraintFactory extends PhoneTransformConstraintFactory {

    public static CueConstraintFactory createInstance() {
        return new CueConstraintFactory();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.SubformConstraintFactory
     * #createConstraint(java.lang.Object)
     */
    @Override
    public CueConstraint createConstraint(PhoneTransform offender) {
        return CueConstraint.createInstance(offender);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * constraints.factories.ConstraintFactory#addFormMapping(gen.mapping
     * .FormMapping)
     */
    @Override
    public void addFormMapping(FormMapping fm) {
        if (fm instanceof SfPfMapping) {
            addTransgressor((SfPfMapping) fm);
        }
    }

}
