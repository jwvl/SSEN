/**
 *
 */
package simulate.french.sixlevel.constraints;

import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.morphosyntax.MorphologicalWord;
import grammar.levels.predefined.BiPhonSix;
import constraints.FormConstraint;

/**
 * @author jwvl
 * @date Sep 19, 2015
 * First version. Universal over all syntactic categories. Punishes
 */
public class AnalyzeConstraint extends FormConstraint<MForm> {

    /**
     * @param rightLevel
     */
    public AnalyzeConstraint() {
        super(BiPhonSix.getMformLevel());
    }

    @Override
    public String toString() {
        return "Analyze";
    }

    /* (non-Javadoc)
     * @see constraints.FormConstraint#getNumViolations(forms.Form)
     */
    @Override
    public int getNumViolations(MForm f) {
        int result = 0;
        for (MorphologicalWord morphologicalWord : f) {
            for (Morpheme m : morphologicalWord) {
                result += (m.size() - 1);
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


}
