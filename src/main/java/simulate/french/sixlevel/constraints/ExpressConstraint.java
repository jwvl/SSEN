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
 */
public class ExpressConstraint extends FormConstraint<MStructure> {

    private final SyntacticCategory syntacticCategory;
    private final AbstractMFeature2 prohibitedFeature;

    private ExpressConstraint(SyntacticCategory syntacticCategory,
                              Attribute attribute) {
        super(BiPhonSix.getMstructureLevel());
        this.syntacticCategory = syntacticCategory;
        this.prohibitedFeature = AbstractMFeature2.getNullInstance(attribute);
    }


    public static ExpressConstraint createFromAffixType(AffixType affixType) {
        return new ExpressConstraint(affixType.getSyntacticCategory(), affixType.getAttribute());
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
            if (containsTransgressiveFeature(syntacticWord)) {
                result += 1;
            }
        }
        return result;
    }

    private boolean containsTransgressiveFeature(SyntacticWord syntacticWord) {
        if (syntacticWord.getSyntacticCategory().equals(syntacticCategory)) {
            for (MElement element : syntacticWord) {
                AbstractMFeature2 feature = element.getFeature();
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
     * @see constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Express ");
        result.append(syntacticCategory.toString()).append(" - ");
        result.append(prohibitedFeature.attribute);
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
