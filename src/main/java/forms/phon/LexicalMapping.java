/**
 *
 */
package forms.phon;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import forms.morphosyntax.Morpheme;
import forms.primitives.Submapping;
import forms.primitives.segment.PhoneSubForm;

import java.util.List;

/**
 * @author jwvl
 * @date Aug 2, 2015
 * Caches since there are a lot of these...
 */
public class LexicalMapping extends Submapping<Morpheme, PhoneSubForm> {
    private static Table<Morpheme,PhoneSubForm,LexicalMapping> instances = HashBasedTable.create();
    /**
     * @param s
     * @param t
     */
    private LexicalMapping(Morpheme s, PhoneSubForm t) {
        super(s, t);
    }

    private static LexicalMapping createNew(Morpheme morpheme, PhoneSubForm phoneSubForm) {
        LexicalMapping result = new LexicalMapping(morpheme,phoneSubForm);
        instances.put(morpheme,phoneSubForm,result);
        return result;
    }

    public static LexicalMapping of(Morpheme morpheme, PhoneSubForm phoneSubForm) {
        LexicalMapping instance = instances.get (morpheme,phoneSubForm);
        if (instance == null) {
            instance = createNew(morpheme,phoneSubForm);
        }
        return instance;
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
