package simulate.phonetic.vowelsystems.subgens;

import forms.Form;
import gen.mapping.FormMapping;
import simulate.phonetic.vowelsystems.levels.FeatureValueForm;
import simulate.phonetic.vowelsystems.levels.UnderlyingVowelForm;

public class VowelSfUfMapping extends FormMapping {

    private VowelSfUfMapping(Form f, Form g) {
        super(f, g);
    }

    public VowelSfUfMapping(FeatureValueForm left, UnderlyingVowelForm right) {
        super(left,right);
    }
}
