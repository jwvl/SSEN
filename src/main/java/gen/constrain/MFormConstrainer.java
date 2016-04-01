/**
 *
 */
package gen.constrain;

import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.morphosyntax.MorphologicalWord;

/**
 * @author jwvl
 * @date 25/09/2015
 * Constrains MForm generation to forms with a concept at the left or right edge.
 */
public class MFormConstrainer implements GenConstrainer<MForm> {

    /* (non-Javadoc)
     * @see gen.constrain.GenConstrainer#canGenerate(forms.Form)
     */
    @Override
    public boolean canGenerate(MForm form) {
        for (MorphologicalWord mw : form) {
            Morpheme[] elements = mw.elementsAsArray();
            int finalElement = elements.length - 1;
            if (!elements[0].hasConceptFeature() & !elements[finalElement].hasConceptFeature()) {
                return false;
            }
        }
        return true;
    }

}
