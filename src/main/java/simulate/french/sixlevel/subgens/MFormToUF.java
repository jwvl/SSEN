/**
 *
 */
package simulate.french.sixlevel.subgens;

import com.google.common.base.Function;
import com.google.common.collect.Sets;
import forms.morphosyntax.MForm;
import forms.morphosyntax.Morpheme;
import forms.morphosyntax.MorphologicalWord;
import forms.phon.LexicalMapping;
import forms.phon.flat.UnderlyingForm;
import forms.primitives.segment.PhoneSubForm;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import gen.mapping.specific.MfUfMapping;
import gen.subgen.SubGen;
import grammar.levels.predefined.BiPhonSix;
import simulate.french.sixlevel.constraints.factories.LexicalConstraintFactory;
import simulate.french.sixlevel.helpers.LexicalHypothesisRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date May 9, 2015
 */
public class MFormToUF extends SubGen<MForm, UnderlyingForm> {
    public static int counter = 0;
    private LexicalHypothesisRepository lexicalHypotheses;

    public MFormToUF(LexicalHypothesisRepository lexicalHypotheses) {
        super(BiPhonSix.getMformLevel(), BiPhonSix.getUnderlyingFormLevel());
        this.lexicalHypotheses = lexicalHypotheses;
        addConstraintFactory(new LexicalConstraintFactory(lexicalHypotheses));
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.SubGen#getRightFunction()
     */
    @Override
    protected Function<MForm, SubCandidateSet> getRightFunction() {
        return new Function<MForm, SubCandidateSet>() {

            public SubCandidateSet apply(MForm input) {
                ArrayList<Set<PhoneSubForm>> setList = new ArrayList<Set<PhoneSubForm>>(
                        input.size());
                List<Morpheme> allMorphemes = input.getMorphemes();
                for (Morpheme m : allMorphemes) {
                    setList.add(Sets.newHashSet(lexicalHypotheses
                            .getCandidates(m)));
                }
                int[] wordBoundaries = getWordBoundaries(input);

                Set<List<PhoneSubForm>> asPhoneStrings = Sets
                        .cartesianProduct(setList);
                List<FormMapping> result = new ArrayList<FormMapping>(
                        asPhoneStrings.size());

                for (List<PhoneSubForm> phoneStringList : asPhoneStrings) {
                    LexicalMapping[] mappings = createLexicalMappings(allMorphemes, phoneStringList);
                    UnderlyingForm uf = UnderlyingForm
                            .createInstance(phoneStringList, wordBoundaries);
                    FormMapping toAdd = MfUfMapping.createInstance(input, uf, mappings);
                    result.add(toAdd);
                }

                return SubCandidateSet.of(result);
            }

            private LexicalMapping[] createLexicalMappings(List<Morpheme> allMorphemes,
                                                           List<PhoneSubForm> phoneStringList) {
                LexicalMapping[] result = new LexicalMapping[allMorphemes.size()];
                for (int i = 0; i < result.length; i++) {
                    result[i] = LexicalMapping.of(allMorphemes.get(i), phoneStringList.get(i));
                }
                return result;
            }

            private int[] getWordBoundaries(MForm input) {
                int[] result = new int[input.size() - 1];
                List<MorphologicalWord> mWords = input.elementsAsList();
                int previous = 0;
                for (int i = 0; i < result.length; i++) {
                    result[i] = mWords.get(i).size() + previous;
                    previous = result[i];
                }
                return result;
            }
        };

    }


}
