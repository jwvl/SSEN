/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.phon.flat.SurfaceForm;
import forms.phon.syllable.ConsonantCluster;
import forms.phon.syllable.SonorityProfile;
import gen.rule.string.Side;
import grammar.levels.predefined.BiPhonSix;
import constraints.FormConstraint;

import java.util.Objects;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public class ClusterConstraint extends FormConstraint<SurfaceForm> {

    private final ConsonantCluster forbiddenCluster;

    private ClusterConstraint(ConsonantCluster forbiddenCluster) {
        super(BiPhonSix.getSurfaceFormLevel());
        this.forbiddenCluster = forbiddenCluster;
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.FormConstraint#getNumViolations(forms.Form)
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
     * @see constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        if (forbiddenCluster == ConsonantCluster.EMPTY_ONSET) {
            return ("NoOnset");
        } else if (forbiddenCluster == ConsonantCluster.EMPTY_CODA) {
            return ("NoCoda");

        } else return "*" + forbiddenCluster.toString();
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
     * @return
     */
    public static ClusterConstraint createInstance(
            ConsonantCluster forbiddenProfile) {
        return new ClusterConstraint(forbiddenProfile);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ClusterConstraint that = (ClusterConstraint) o;
        return Objects.equals(forbiddenCluster, that.forbiddenCluster);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), forbiddenCluster);
    }
}
