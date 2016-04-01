/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import forms.phon.flat.SurfaceForm;
import forms.phon.syllable.ConsonantCluster;
import forms.phon.syllable.SonorityProfile;
import simulate.french.sixlevel.constraints.OnsetCodaConstraint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public class OnsetCodaConstraintFactory extends SubformConstraintFactory<SurfaceForm, ConsonantCluster> {


    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.SubformConstraintFactory
     * #createConstraint(java.lang.Object)
     */
    @Override
    public OnsetCodaConstraint createConstraint(ConsonantCluster offender) {

        return OnsetCodaConstraint.createInstance(offender);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.SubformConstraintFactory
     * #getOffenders(graph.Transgressor)
     */
    @Override
    public Collection<ConsonantCluster> findOffenders(SurfaceForm transgressor) {
        SonorityProfile[] profiles = transgressor.getSonorityProfiles();
        List<ConsonantCluster> result = new ArrayList<ConsonantCluster>(profiles.length * 2);
        for (SonorityProfile sonorityProfile : profiles) {
            ConsonantCluster[] onsetAndCoda = ConsonantCluster.getOnsetAndCoda(sonorityProfile);
            if (!onsetAndCoda[0].isEmpty()) {
                result.add(onsetAndCoda[0]);
            }
            if (!onsetAndCoda[1].isEmpty()) {
                result.add(onsetAndCoda[1]);
            }

        }
        return result;
    }

}
