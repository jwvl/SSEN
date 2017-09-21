package simulate.phonetic.vowelsystems.constraints;

import constraints.MappingConstraint;
import forms.primitives.feature.SurfaceRangeFeature;
import gen.mapping.FormMapping;
import simulate.phonetic.vowelsystems.levels.FeatureValueForm;
import simulate.phonetic.vowelsystems.levels.UnderlyingVowelForm;
import simulate.phonetic.vowelsystems.levels.VowelSimLevels;
import simulate.phonetic.vowelsystems.subgens.VowelSfUfMapping;

public class VowelFaithConstraint extends MappingConstraint<VowelSfUfMapping> {
    private final SurfaceRangeFeature inFeature;
    private final String outfeature;

    public VowelFaithConstraint(double initialBias, SurfaceRangeFeature inFeature, String outfeature) {
        super(VowelSimLevels.getSurfaceFormLevel(), initialBias);
        this.inFeature = inFeature;
        this.outfeature = outfeature;
    }

    @Override
    public int getNumViolations(FormMapping f) {
        if (f.left() instanceof FeatureValueForm && f.right() instanceof UnderlyingVowelForm) {
            FeatureValueForm l = (FeatureValueForm) f.left();
            UnderlyingVowelForm r = (UnderlyingVowelForm) f.right();
            if (l.getFeatures().contains(inFeature) && r.getContent().equals(outfeature)) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "*FAITH "+inFeature +" -> "+rightLevel.getInfo().bracket(outfeature);
    }

    @Override
    public boolean caches() {
        return false;
    }
}
