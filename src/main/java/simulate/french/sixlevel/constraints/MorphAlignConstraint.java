/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.morphosyntax.*;
import gen.rule.string.Side;
import grammar.levels.predefined.BiPhonSix;
import constraints.FormConstraint;

import java.util.Objects;

/**
 * @author jwvl
 * @date Jul 31, 2015
 */
public class MorphAlignConstraint extends FormConstraint<MForm> {

    private final SyntacticCategory relevantCategory;
    private final AttributeSet offendingAttributes;
    private final Side side;
    private final String name;

    public MorphAlignConstraint(SyntacticCategory syntacticCategory,
                                AttributeSet attributeSet, Side side) {
        super(BiPhonSix.getMformLevel());
        this.relevantCategory = syntacticCategory;
        this.offendingAttributes = attributeSet;
        this.side = side;
        this.name = createNameString();
    }

    /**
     * @return
     */
    private String createNameString() {
        StringBuilder result = new StringBuilder("Align-");
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
        //System.out.printf("DEBUG: %s violates %s %d times%n",f,this,result);
        return result;
    }

    /**
     * @param morphologicalWord
     * @return
     */
    private int getViolations(MorphologicalWord morphologicalWord) {
        int result = 0;
        if (morphologicalWord.getCategory().equals(relevantCategory)) {
            Morpheme[] morphemes = morphologicalWord.elementsAsArray();
            for (int i = 0; i < morphologicalWord.size(); i++) {
                Morpheme morpheme = morphemes[i];
                AttributeSet offenders = morpheme.getAttributes();
                if (offenders.equals(offendingAttributes)) {
                    result += calculateViolations(i, morphemes.length);
                }
            }

        }
        return result;
    }

    /**
     * @param i
     * @param size
     * @return
     */
    private int calculateViolations(int i, int size) {
        if (side == Side.LEFT) {
            return i;
        } else if (side == Side.RIGHT) {
            return size - (i + 1);
        } else {
            return 0;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        return name;
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.Constraint#caches()
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MorphAlignConstraint that = (MorphAlignConstraint) o;
        return relevantCategory == that.relevantCategory &&
                Objects.equals(offendingAttributes, that.offendingAttributes) &&
                side == that.side;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), relevantCategory, offendingAttributes, side);
    }
}
