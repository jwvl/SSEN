/**
 *
 */
package forms.phon;

import forms.morphosyntax.Morpheme;
import forms.primitives.Submapping;
import forms.primitives.segment.PhoneSubForm;

import java.util.List;

/**
 * @author jwvl
 * @date Aug 2, 2015
 */
public class LexicalMapping extends Submapping<Morpheme, PhoneSubForm> {

    /**
     * @param s
     * @param t
     */
    private LexicalMapping(Morpheme s, PhoneSubForm t) {
        super(s, t);
    }

    public static LexicalMapping of(Morpheme morpheme, PhoneSubForm phoneSubForm) {
        return new LexicalMapping(morpheme, phoneSubForm);
    }

    public String toString() {
        return String.format("%s -> %s", left(), right());
    }

    /*
    NB assumes that they have the same number of elements!
     */
    public static LexicalMapping[] of(List<Morpheme> morphemes, List<PhoneSubForm> phoneSubForms) {
        LexicalMapping[] result = new LexicalMapping[morphemes.size()];
        for (int i = 0; i < morphemes.size(); i++) {
            result[i] = LexicalMapping.of(morphemes.get(i), phoneSubForms.get(i));
        }
        return result;
    }


}
