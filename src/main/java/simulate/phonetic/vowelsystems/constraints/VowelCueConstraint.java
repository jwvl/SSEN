package simulate.phonetic.vowelsystems.constraints;

import constraints.MappingConstraint;
import forms.primitives.feature.ScaleFeature;
import forms.primitives.feature.SurfaceRangeFeature;
import gen.mapping.FormMapping;
import simulate.phonetic.vowelsystems.levels.FeatureValueForm;
import simulate.phonetic.vowelsystems.levels.PhoneticValueForm;
import simulate.phonetic.vowelsystems.levels.VowelSimLevels;
import simulate.phonetic.vowelsystems.subgens.VowelPfSfMapping;

public class VowelCueConstraint extends MappingConstraint<VowelPfSfMapping> {
    private final ScaleFeature in;
    private final SurfaceRangeFeature out;

    public VowelCueConstraint(double initialBias, ScaleFeature in, SurfaceRangeFeature out) {
        super(VowelSimLevels.getPhoneticLevel(), initialBias);
        this.in = in;
        this.out = out;
    }

    @Override
    public int getNumViolations(FormMapping f) {
        PhoneticValueForm left;
        FeatureValueForm right;
        if (f.left() instanceof PhoneticValueForm && f.right() instanceof FeatureValueForm) {
            left = (PhoneticValueForm) f.left();
            right = (FeatureValueForm) f.right();
            if (left.getFeatures().contains(in) && right.getFeatures().contains(out)) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "CUE *"+in+" -> "+out;
    }

    @Override
    public boolean caches() {
        return false;
    }
}
