/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.phon.flat.SurfaceForm;
import forms.phon.syllable.ConsonantCluster;
import forms.phon.syllable.SonorityProfile;
import gen.rule.string.Side;
import grammar.levels.predefined.BiPhonSix;
import ranking.constraints.FormConstraint;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public class OnsetCodaConstraint extends FormConstraint<SurfaceForm> {

    private final ConsonantCluster forbiddenCluster;

    private OnsetCodaConstraint(ConsonantCluster forbiddenCluster) {
        super(BiPhonSix.getSurfaceFormLevel());
        this.forbiddenCluster = forbiddenCluster;
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.FormConstraint#getNumViolations(forms.Form)
     */
    @Override
    public int getNumViolations(SurfaceForm f) {
        int result = 0;
        Side side = forbiddenCluster.getSide();
        for (SonorityProfile sonorityProfile : f.getSonorityProfiles()) {
            ConsonantCluster toCheck;
            if (side == Side.LEFT) {
                toCheck = ConsonantCluster.getOnset(sonorityProfile);
            } else {
                toCheck = ConsonantCluster.getCoda(sonorityProfile);
            }
            if (toCheck.equals(forbiddenCluster)) {
                result += 1;
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        return "*" + forbiddenCluster.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.Constraint#caches()
     */
    @Override
    public boolean caches() {
        return false;
    }

    /**
     * @return
     */
    public static OnsetCodaConstraint createInstance(
            ConsonantCluster forbiddenProfile) {
        return new OnsetCodaConstraint(forbiddenProfile);
    }

}
