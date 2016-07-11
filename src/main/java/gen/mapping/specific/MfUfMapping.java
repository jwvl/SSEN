/**
 *
 */
package gen.mapping.specific;

import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.phon.LexicalMapping;
import forms.phon.flat.UnderlyingForm;
import forms.primitives.segment.PhoneSubForm;
import gen.mapping.FormMapping;

import java.util.Arrays;
import java.util.List;

/**
 * @author jwvl
 * @date Jul 23, 2015
 */
public class MfUfMapping extends FormMapping {

    private final LexicalMapping[] lexicalMappings;
    private final int hashCode;

    /**
     * @param f
     * @param g
     */
    private MfUfMapping(MForm f, UnderlyingForm g, LexicalMapping[] mappings) {
        super(f, g);
        this.lexicalMappings = mappings;
        this.hashCode = computeHashCode();
    }


    /**
     * @param top
     * @param bottom
     */
    public static MfUfMapping createInstance(MForm top, UnderlyingForm bottom) {
        LexicalMapping[] submappings = createSubmappings(top, bottom);
        MfUfMapping result = new MfUfMapping(top, bottom, submappings);
        return result;
    }

    /**
     * @param top
     * @param bottom
     */
    public static MfUfMapping createInstance(MForm top, UnderlyingForm bottom, LexicalMapping[] mappings) {
        MfUfMapping result = new MfUfMapping(top, bottom, mappings);
//		System.out.println("Created mapping: " +result);
        return result;
    }

    /**
     * @param top
     * @param bottom
     * @return
     */
    private static LexicalMapping[] createSubmappings(MForm top, UnderlyingForm bottom) {
        List<Morpheme> morphemes = top.getMorphemes();
        LexicalMapping[] result = new LexicalMapping[morphemes.size()];
        PhoneSubForm[] phoneSubForms = bottom.getPhoneSubforms();
        for (int i = 0; i < result.length; i++) {
            result[i] = LexicalMapping.of(morphemes.get(i), phoneSubForms[i]);
        }
        return result;
    }

    /**
     * @return
     */
    public LexicalMapping[] getLexicalMappings() {
        return lexicalMappings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MfUfMapping that = (MfUfMapping) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.deepEquals(lexicalMappings, that.lexicalMappings);

    }

    public int computeHashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.deepHashCode(lexicalMappings);
        return result;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
