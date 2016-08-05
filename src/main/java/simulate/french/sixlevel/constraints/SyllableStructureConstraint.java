/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.phon.flat.SurfaceForm;
import forms.phon.syllable.SonorityProfile;
import grammar.levels.predefined.BiPhonSix;
import constraints.FormConstraint;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public class SyllableStructureConstraint extends FormConstraint<SurfaceForm> {

    private final SonorityProfile forbiddenProfile;

    private SyllableStructureConstraint(SonorityProfile forbiddenProfile) {
        super(BiPhonSix.getSurfaceFormLevel());
        this.forbiddenProfile = forbiddenProfile;
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.FormConstraint#getNumViolations(forms.Form)
     */
    @Override
    public int getNumViolations(SurfaceForm f) {
        int result = 0;
        for (SonorityProfile sonorityProfile : f.getSonorityProfiles()) {
            if (sonorityProfile.equals(forbiddenProfile)) {
                result += 1;
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        return "*" + forbiddenProfile.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.Constraint#caches()
     */
    @Override
    public boolean caches() {
        return false;
    }

    /**
     * @param offender
     * @return
     */
    public static SyllableStructureConstraint createInstance(
            SonorityProfile forbiddenProfile) {
        return new SyllableStructureConstraint(forbiddenProfile);
    }

}
