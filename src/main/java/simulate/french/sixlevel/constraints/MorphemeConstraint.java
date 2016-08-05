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
 * Simple constraint that forbids a given morpheme.
 */
public class MorphemeConstraint extends FormConstraint<MForm> {
    private final Morpheme morpheme;

    /**
     * @param morpheme
     */
    public MorphemeConstraint(Morpheme morpheme) {
        super(BiPhonSix.getMformLevel());
        this.morpheme = morpheme;
    }

    @Override
    public String toString() {
        return "*MOR "+morpheme;
    }

    /* (non-Javadoc)
     * @see constraints.FormConstraint#getNumViolations(forms.Form)
     */
    @Override
    public int getNumViolations(MForm f) {
        int result = 0;
        for (MorphologicalWord morphologicalWord : f) {
            for (Morpheme m : morphologicalWord) {
                if (m.equals(morpheme)) {
                    result +=1;
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


}
