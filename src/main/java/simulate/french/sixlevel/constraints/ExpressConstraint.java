/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.morphosyntax.*;
import forms.primitives.feature.AbstractMFeature;
import forms.primitives.feature.MorphologicalFeature;
import grammar.levels.predefined.BiPhonSix;
import ranking.constraints.FormConstraint;

/**
 * @author jwvl
 * @date Jul 28, 2015
 */
public class ExpressConstraint extends FormConstraint<MStructure> {

    private final SyntacticCategory syntacticCategory;
    private final MorphologicalFeature prohibitedFeature;

    private ExpressConstraint(SyntacticCategory syntacticCategory,
                              String attribute) {
        super(BiPhonSix.getMstructureLevel());
        this.syntacticCategory = syntacticCategory;
        this.prohibitedFeature = MorphologicalFeature.getNullInstance(attribute);
    }


    public static ExpressConstraint createFromAffixType(AffixType affixType) {
        return new ExpressConstraint(affixType.getSyntacticCategory(), affixType.getAttribute());
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.FormConstraint#getNumViolations(forms.Form)
     */
    @Override
    public int getNumViolations(MStructure mStructure) {
        int result = 0;
        for (SyntacticWord syntacticWord : mStructure) {
            if (containsTransgressiveFeature(syntacticWord)) {
                result += 1;
            }
        }
        return result;
    }

    private boolean containsTransgressiveFeature(SyntacticWord syntacticWord) {
        if (syntacticWord.getSyntacticCategory().equals(syntacticCategory)) {
            for (MElement element : syntacticWord) {
                AbstractMFeature feature = element.getFeature();
                if (feature.equals(prohibitedFeature)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Express ");
        result.append(syntacticCategory.toString()).append(" - ");
        result.append(prohibitedFeature.getAttribute());
        return result.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.Constraint#caches()
     */
    @Override
    public boolean caches() {
        return false;
    }

}
