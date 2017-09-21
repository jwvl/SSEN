package simulate.phonetic.vowelsystems.levels;

import forms.primitives.Subform;
import forms.primitives.feature.ScaleFeature;
import grammar.levels.Level;
import grammar.levels.LevelInfo;

import java.util.List;

public class PhoneticValueForm extends RangeListForm<ScaleFeature> {

    public PhoneticValueForm(List<ScaleFeature> features) {
        super(features);
    }


    @Override
    public Level getLevel() {
        return VowelSimLevels.getPhoneticLevel();
    }

    @Override
    public int getLevelIndex() {
        return getLevel().myIndex();
    }

    @Override
    public String toBracketedString() {
        LevelInfo levelInfo = getLevel().getInfo();
        StringBuilder sb = new StringBuilder();
        sb.append(levelInfo.getLeftBracket());
        int size = getNumSubForms();
        for (int i=0; i < size; i++) {
            int value = getContents()[i];
                String toAppend = getFeatures().get(i).toString();
                sb.append(toAppend);
            if (i < size-1) {
                sb.append(", ");
            }
        }
        sb.append(levelInfo.getRightBracket());
        return sb.toString();
    }

    @Override
    public String toString() {
        return toBracketedString();
    }

    @Override
    public int countSubform(Subform sf) {
        return 0;
    }

}
