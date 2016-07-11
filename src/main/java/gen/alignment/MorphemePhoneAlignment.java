/**
 *
 */
package gen.alignment;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import forms.morphosyntax.MorphemeCollection;
import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.phon.LexicalMapping;
import forms.primitives.segment.PhoneSubForm;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date May 9, 2015
 */
public class MorphemePhoneAlignment {

    NonCrossingAlignmentIndex index;
    private final List<LexicalMapping> lexicalMappings;

    /**
     * @param topForm
     * @param bottomForm
     */
    private MorphemePhoneAlignment(MorphemeCollection morphemes, PhoneSubForm phoneSubForm,
                                   NonCrossingAlignmentIndex index) {
        this.index = index;
        lexicalMappings = getDownGroupings(morphemes, phoneSubForm);
    }

    public static MorphemePhoneAlignment create(MForm mForm, PhoneSubForm phoneSubForm, AlignmentIndex ai) {
        return new MorphemePhoneAlignment(mForm.toMorphemeCollection(), phoneSubForm, ai.toNonCrossingAlignmentIndex());
    }

    public Collection<LexicalMapping> getLexicalSubmappings() {
        return lexicalMappings;
    }

    /**
     * @param alignmentArray
     * @param phoneSubForm
     * @param mWord
     * @return
     */
    private List<LexicalMapping> getDownGroupings(MorphemeCollection morphemeCollection,
                                                  PhoneSubForm phoneSubForm) {
        Builder<LexicalMapping> builder = ImmutableList.builder();
        int startIndex = 0;
        byte[] asByteArray = phoneSubForm.getContents();
        Morpheme[] morphemes = morphemeCollection.elementsAsArray();
        int[] lengths = index.lengthPerGroup;
        for (int i = 0; i < morphemes.length; i++) {
            int stopIndex = startIndex + lengths[i];
            PhoneSubForm partition;
            if (stopIndex > startIndex) {
                byte[] subset = Arrays.copyOfRange(asByteArray, startIndex, stopIndex);
                partition = PhoneSubForm.createFromByteArray(subset);
            } else {
                partition = PhoneSubForm.NULL_FORM;
            }

            builder.add(LexicalMapping.of(morphemes[i], partition));

            startIndex = stopIndex;
        }
        return builder.build();

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MorphemePhoneAlignment{");
        sb.append("index=").append(index);
        sb.append(", lexicalMappings=").append(lexicalMappings);
        sb.append('}');
        return sb.toString();
    }
}
