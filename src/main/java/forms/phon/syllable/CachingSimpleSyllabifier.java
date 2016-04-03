/**
 *
 */
package forms.phon.syllable;

import forms.primitives.boundary.EdgeIndex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jwvl
 * @date 24/02/2016
 */
public class CachingSimpleSyllabifier extends SimpleSyllabifier {
    private Map<SonorityProfile, List<EdgeIndex>> cached;

    /**
     *
     */
    public CachingSimpleSyllabifier() {
        super();
        cached = new HashMap<SonorityProfile, List<EdgeIndex>>();
    }

    @Override
    public List<EdgeIndex> getSyllabifications(byte[] asByteArray) {
        return retrieveOrCalculateSyllabifications(asByteArray);
    }

    @Override
    public SyllabifierType getType() {
        return SyllabifierType.SIMPLE;
    }

    private List<EdgeIndex> retrieveOrCalculateSyllabifications(byte[] asByteArray) {
        SonorityProfile profile = SonorityProfile.fromBytes(asByteArray);
        List<EdgeIndex> result = cached.get(profile);
        if (result == null) {
            result = super.getSyllabifications(asByteArray);
            cached.put(profile, result);
        }
        return result;
    }


}
