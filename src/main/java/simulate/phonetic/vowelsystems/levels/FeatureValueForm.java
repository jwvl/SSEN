package simulate.phonetic.vowelsystems.levels;

import forms.primitives.Subform;
import forms.primitives.feature.SurfaceRangeFeature;
import grammar.levels.Level;
import grammar.levels.LevelInfo;

import java.util.List;

public class FeatureValueForm extends RangeListForm<SurfaceRangeFeature> {

    public FeatureValueForm(List<SurfaceRangeFeature> features) {
        super(features);
    }


    @Override
    public Level getLevel() {
        return VowelSimLevels.getSurfaceFormLevel();
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
    public int countSubform(Subform sf) {
        return getFeatures().contains(sf) ? 1 : 0;
    }
}
