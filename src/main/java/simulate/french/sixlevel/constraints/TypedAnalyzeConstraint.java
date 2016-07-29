/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.morphosyntax.AffixType;
import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.morphosyntax.MorphologicalWord;
import grammar.levels.predefined.BiPhonSix;
import ranking.constraints.FormConstraint;

/**
 * @author jwvl
 * @date Sep 19, 2015
 * First version. Universal over all syntactic categories. Punishes synthesis (coalescence) of
 * M-features within Morphemes.
 */
public class TypedAnalyzeConstraint extends FormConstraint<MForm> {
    private final AffixType affixType;

    /**
     * @param affixType
     */
    public TypedAnalyzeConstraint(AffixType affixType) {
        super(BiPhonSix.getMformLevel());
        this.affixType = affixType;
    }

    @Override
    public String toString() {
        return "Analyze";
    }

    /* (non-Javadoc)
     * @see ranking.constraints.FormConstraint#getNumViolations(forms.Form)
     */
    @Override
    public int getNumViolations(MForm f) {
        int result = 0;
        for (MorphologicalWord morphologicalWord : f) {
            for (Morpheme m : morphologicalWord) {
                if (m.containsAffixType(affixType)) {
                    result += m.size()-1;
                }
            }
        }
        //System.out.printf("%s violates ANALYZE %d times%n",f,result);
        return result;
    }

    /* (non-Javadoc)
     * @see ranking.constraints.Constraint#caches()
     */
    @Override
    public boolean caches() {

        return false;
    }


}
