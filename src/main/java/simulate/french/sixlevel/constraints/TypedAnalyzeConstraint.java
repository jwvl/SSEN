/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.morphosyntax.AffixType;
import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.morphosyntax.MorphologicalWord;
import grammar.levels.predefined.BiPhonSix;
import constraints.FormConstraint;

import java.util.Objects;

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
        return "Analyze " + affixType;
    }

    /* (non-Javadoc)
     * @see constraints.FormConstraint#getNumViolations(forms.Form)
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
     * @see constraints.Constraint#caches()
     */
    @Override
    public boolean caches() {

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TypedAnalyzeConstraint that = (TypedAnalyzeConstraint) o;
        return Objects.equals(affixType, that.affixType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), affixType);
    }
}
