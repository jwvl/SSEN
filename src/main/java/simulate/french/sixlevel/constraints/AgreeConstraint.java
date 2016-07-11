/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.morphosyntax.AffixType;
import forms.morphosyntax.Agreement;
import forms.morphosyntax.MStructure;
import grammar.levels.predefined.BiPhonSix;
import ranking.constraints.FormConstraint;

import java.util.Collection;

/**
 * @author jwvl
 * @date Jul 28, 2015
 */
public class AgreeConstraint extends FormConstraint<MStructure> {

    private final AffixType affixType;

    public AgreeConstraint(AffixType affixType) {
        super(BiPhonSix.getMstructureLevel());
        this.affixType = affixType;
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.FormConstraint#getNumViolations(forms.Form)
     */
    @Override
    public int getNumViolations(MStructure mStructure) {
        int result = 0;
        Collection<Agreement> agreements = mStructure.getAgreements();
        for (Agreement a : agreements) {
            if (isViolatingAgreement(a)) {
                result++;
            }
        }
        return result;
    }

    /**
     * @param a
     * @return
     */
    private boolean isViolatingAgreement(Agreement a) {
        return (a.getAffixType().equals(affixType) && !a.valuesAgree());
    }

    /*
     * (non-Javadoc)
     *
     * @see ranking.constraints.Constraint#toString()
     */
    @Override
    public String toString() {
        return new StringBuilder("Agree ").append(affixType).toString();
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
