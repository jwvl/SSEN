/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import forms.phon.PhoneTransform;
import forms.phon.flat.PhoneticForm;
import forms.phon.flat.SurfaceForm;
import gen.mapping.FormMapping;
import gen.mapping.specific.SfPfMapping;
import ranking.constraints.factories.PhoneTransformConstraintFactory;
import simulate.french.sixlevel.constraints.CueConstraint;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public class CueConstraintFactory extends PhoneTransformConstraintFactory<SurfaceForm, PhoneticForm> {

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
     * ranking.constraints.factories.ConstraintFactory#addFormMapping(gen.mapping
     * .FormMapping)
     */
    @Override
    public void addFormMapping(FormMapping fm) {
        if (fm instanceof SfPfMapping) {
            addTransgressor((SfPfMapping) fm);
        }
    }

}
