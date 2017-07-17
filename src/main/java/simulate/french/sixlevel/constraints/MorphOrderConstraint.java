package simulate.french.sixlevel.constraints;

import constraints.FormConstraint;
import forms.morphosyntax.*;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;

/**
 * Created by janwillem on 01/07/2017.
 */
public class MorphOrderConstraint extends FormConstraint<MForm> {
    private static Level leftLevel = BiPhonSix.getMformLevel();
    private final SyntacticCategory syntacticCategory;
    private final Attribute first;
    private final Attribute second; // meaning: first must precede second, i.e. violation if second < first

    protected MorphOrderConstraint(Level rightLevel, SyntacticCategory syntacticCategory, Attribute first, Attribute second) {
        super(rightLevel);
        this.syntacticCategory = syntacticCategory;
        this.first = first;
        this.second = second;
    }

    public MorphOrderConstraint(SyntacticCategory category, Attribute first, Attribute second) {
        this(leftLevel, category, first, second);
    }

    @Override
    public int getNumViolations(MForm morphologicalWords) {
        int result = 0;
        for (MorphologicalWord morphologicalWord: morphologicalWords) {
            if (morphologicalWord.getCategory() == syntacticCategory) {
                if (violates(morphologicalWord)) {
                    result += 1;
                }
            }
        }
        return result;
    }

    private boolean violates(MorphologicalWord morphologicalWord) {
        int firstIndex = -1, secondIndex = -1;
        Morpheme[] morphemes = morphologicalWord.elementsAsArray();
        for (int i=0; i < morphemes.length; i++) {
            AttributeSet attributeSet = morphemes[i].getAttributes();
            if (attributeSet.contains(first)) {
                firstIndex = i;
            }
            if (attributeSet.contains(second)) {
                secondIndex = i;
            }
        }
        if (firstIndex < secondIndex && firstIndex >= 0) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "*"+syntacticCategory+"("+first+" < "+second+")";
    }

    @Override
    public boolean caches() {
        return false;
    }
}
