/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import forms.phon.flat.SurfaceForm;
import forms.phon.syllable.SonorityProfile;
import simulate.french.sixlevel.constraints.SyllableStructureConstraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date Jul 26, 2015
 */
public class SyllableStructureConstraintFactory extends SubformConstraintFactory<SurfaceForm, SonorityProfile> {

    private final SonorityProfile[] profiles;

    public SyllableStructureConstraintFactory() {
        super();
        profiles = SonorityProfile.getAllowedProfiles();
        for (SonorityProfile sonorityProfile : profiles) {
            createConstraint(sonorityProfile);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.SubformConstraintFactory
     * #createConstraint(java.lang.Object)
     */
    @Override
    public SyllableStructureConstraint createConstraint(SonorityProfile offender) {
        return SyllableStructureConstraint.createInstance(offender);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.SubformConstraintFactory
     * #getOffenders(graph.Transgressor)
     */
    @Override
    public Collection<SonorityProfile> findOffenders(SurfaceForm transgressor) {
        List<SonorityProfile> result = new ArrayList<SonorityProfile>(Arrays.asList(transgressor.getSonorityProfiles()));
        return result;
    }

}
