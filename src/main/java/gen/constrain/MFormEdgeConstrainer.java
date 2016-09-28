/**
 *
 */
package gen.constrain;

import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.morphosyntax.MorphologicalWord;
import gen.rule.string.Side;

/**
 * @author jwvl
 * @date 25/09/2015
 * Constrains MForm generation to forms with a concept at the left or right edge.
 */
public class MFormEdgeConstrainer implements GenConstrainer<MForm> {

    /* (non-Javadoc)
     * @see gen.constrain.GenConstrainer#canGenerate(forms.Form)
     */
    @Override
    public boolean canGenerate(MForm form) {
        int numMorphologicalWords = form.getNumSubForms();
        Side[] sides = new Side[numMorphologicalWords];
        for (int i=0; i < numMorphologicalWords; i++) {
            MorphologicalWord mw = form.elementsAsArray()[i];
            sides[i] = getConceptEdge(mw);
            // First check: are concepts at edge?
            if (sides[i] == Side.NEITHER) {
                return false;
            }
        }
        Side firstSide = sides[0];
        // Second check: all sides the same?
        for (int i =1; i < numMorphologicalWords; i++) {
            if (sides[i] != firstSide) {
                return false;
            }
        }
        return true;
    }

    private Side getConceptEdge(MorphologicalWord mw) {
        Morpheme[] elements = mw.elementsAsArray();
        int finalElement = elements.length - 1;
        if (elements[0].hasConceptFeature()) {
            return Side.LEFT;
        } else if (elements[finalElement].hasConceptFeature()) {
            return Side.RIGHT;
        } else {
            return Side.NEITHER;
        }
    }

}
