/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.morphosyntax.*;
import gen.rule.string.Side;
import grammar.levels.predefined.BiPhonSix;
import ranking.constraints.FormConstraint;

import java.util.List;

/**
 * @author jwvl
 * @date Jul 31, 2015
 */
public class AffixPositionConstraint extends FormConstraint<MForm> {

    private final SyntacticCategory relevantCategory;
    private final AttributeSet offendingAttributes;
    private final Side side;

    /**
     * @param leftLevel
     */
    public AffixPositionConstraint(SyntacticCategory syntacticCategory,
                                   AttributeSet attributeSet, Side side) {
        super(BiPhonSix.getMformLevel());
        this.relevantCategory = syntacticCategory;
        this.offendingAttributes = attributeSet;
        this.side = side;
    }

    /**
     * @return
     */
    private String getNameString() {
        StringBuilder result = new StringBuilder("Align ");
        result.append(side.abbreviation).append(" ").
                append(relevantCategory.toString()).append("-").
                append(offendingAttributes.toString());
        return result.toString();
    }

    /**
     * Returns the number of violations inflicted by affixes in this Form.
     */
    @Override
    public int getNumViolations(MForm f) {
        int result = 0;
        for (MorphologicalWord morphologicalWord : f) {
            result += getViolations(morphologicalWord);
        }
        return result;
    }

    /**
     * @param morphologicalWord
     * @return
     */
    private int getViolations(MorphologicalWord morphologicalWord) {
        int result = 0;
        if (morphologicalWord.getCategory().equals(relevantCategory)) {
            List<Morpheme> morphemes = morphologicalWord.elementsAsList();
            int increment = getIncrement(side);
            int conceptIndex = morphologicalWord.getConceptMorphemeIndex();
            for (int i = conceptIndex + increment; i >= 0 && i < morphologicalWord.size(); i += increment) {
                Morpheme toCheck = morphemes.get(i);
                if (toCheck.getAttributes().equals(offendingAttributes))
                    result += (Math.abs(conceptIndex - i)) - 1;
            }
        }
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        return getNameString();
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.Constraint#caches()
     */
    @Override
    public boolean caches() {
        // TODO Auto-generated method stub
        return false;
    }

    private int getIncrement(Side side) {
        if (side == Side.LEFT) {
            return -1;
        } else if (side == Side.RIGHT) {
            return 1;
        } else {
            return 0;
        }
    }

}
