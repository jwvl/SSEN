package simulate.french.sixlevel.constraints;

import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.morphosyntax.MorphologicalWord;
import gen.rule.string.Side;
import grammar.levels.predefined.BiPhonSix;
import ranking.constraints.FormConstraint;

import java.util.Objects;

/**
 * Created by janwillem on 31/03/16.
 * TODO investigate whether this really works or results in harmonically bound forms.
 */
public class StemAlignConstraint extends FormConstraint<MForm> {
    private final Side side;

    public StemAlignConstraint(Side side) {
        super(BiPhonSix.getMformLevel());
        this.side = side;
    }

    @Override
    public int getNumViolations(MForm mForm) {
        int result = 0;
        for (MorphologicalWord morphologicalWord : mForm) {
            result += getViolationsForMorphologicalWord(morphologicalWord);
        }
        return result;
    }

    @Override
    public String toString() {
        return "Align-Stem-" + side.abbreviation;
    }

    @Override
    public boolean caches() {
        return false;
    }

    private int getViolationsForMorphologicalWord(MorphologicalWord word) {
        int result = 0;
        int numMorphemes = word.size();
        int count = 0;
        for (Morpheme morpheme : word) {
            if (morpheme.hasConceptFeature()) {
                result += calculateViolations(count, numMorphemes);
            }
            count++;
        }
        return result;
    }

    private int calculateViolations(int i, int size) {
        if (side == Side.LEFT) {
            return i;
        } else if (side == Side.RIGHT) {
            return size - (i + 1);
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        StemAlignConstraint that = (StemAlignConstraint) o;
        return side == that.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), side);
    }


}
