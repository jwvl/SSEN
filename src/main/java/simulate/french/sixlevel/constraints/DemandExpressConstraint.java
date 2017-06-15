/**
 *
 */
package simulate.french.sixlevel.constraints;

import constraints.FormConstraint;
import forms.morphosyntax.*;
import forms.primitives.feature.AbstractMFeature2;
import grammar.levels.predefined.BiPhonSix;

/**
 * @author jwvl
 * @date Jul 28, 2015
 * Inflicts a violation when a given MStructure DOESN'T express some a given MFeature and has a given syntactic category
 */
public class DemandExpressConstraint extends FormConstraint<MStructure> {

    private final SyntacticCategory syntacticCategory;
    private final AbstractMFeature2 nullFeature;

    private DemandExpressConstraint(SyntacticCategory syntacticCategory,
                                    Attribute attribute) {
        super(BiPhonSix.getMstructureLevel());
        this.syntacticCategory = syntacticCategory;
        this.nullFeature = AbstractMFeature2.getNullInstance(attribute);
    }


    public static DemandExpressConstraint createFromAffixType(AffixType affixType) {
        return new DemandExpressConstraint(affixType.getSyntacticCategory(), affixType.getAttribute());
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.FormConstraint#getNumViolations(forms.Form)
     */
    @Override
    public int getNumViolations(MStructure mStructure) {
        int result = 0;
        for (SyntacticWord syntacticWord : mStructure) {
            if (containsNullFeature(syntacticWord)) {
                result += 1;
            }
        }
        return result;
    }

    private boolean containsNullFeature(SyntacticWord syntacticWord) {
        if (syntacticWord.getSyntacticCategory().equals(syntacticCategory)) {
            for (MElement element : syntacticWord) {
                AbstractMFeature2 feature = element.getFeature();
                if (feature.equals(nullFeature)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Express ");
        result.append(syntacticCategory.toString()).append(" - ");
        result.append(nullFeature.attribute);
        return result.toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see constraints.Constraint#caches()
     */
    @Override
    public boolean caches() {
        return false;
    }

}
