/**
 *
 */
package simulate.french.sixlevel.subgens;

import com.google.common.base.Function;
import com.google.common.collect.Sets;
import forms.phon.Sonority;
import forms.phon.flat.SurfaceForm;
import forms.phon.flat.UnderlyingForm;
import forms.phon.syllable.CachingSimpleSyllabifier;
import forms.phon.syllable.ISyllabifier;
import forms.primitives.boundary.Edge;
import forms.primitives.boundary.EdgeIndex;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import gen.mapping.specific.UfSfMapping;
import gen.rule.edgebased.EdgeBasedRewrite;
import gen.rule.edgebased.EdgeRuleTransformer;
import gen.subgen.SubGen;
import grammar.levels.predefined.BiPhonSix;
import simulate.french.sixlevel.constraints.factories.FaithfulnessConstraintFactory;
import simulate.french.sixlevel.constraints.factories.SyllableStructureConstraintFactory;
import simulate.french.sixlevel.helpers.LexicalHypothesisRepository;
import simulate.french.sixlevel.helpers.PredefinedLiaisonRules;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date May 19, 2015
 */
public class PredefinedUFToSF extends SubGen<UnderlyingForm, SurfaceForm> {

    private ISyllabifier syllabifier;
    private EdgeRuleTransformer edgeRuleTransformer;
    private final int MAX_CONSECUTIVE_C = 3;

    public PredefinedUFToSF(LexicalHypothesisRepository repository) {

        super(BiPhonSix.getUnderlyingFormLevel(), BiPhonSix
                .getSurfaceFormLevel());
        edgeRuleTransformer = EdgeRuleTransformer.createFromRules(PredefinedLiaisonRules.edgeRules);
        syllabifier = new CachingSimpleSyllabifier();
        addConstraintFactory(new FaithfulnessConstraintFactory());
        addConstraintFactory(new SyllableStructureConstraintFactory());
        //addConstraintFactory(new OnsetCodaConstraintFactory());
    }


    /*
     * (non-Javadoc)
     *
     * @see gen.SubGen#getRightFunction()
     */
    @Override
    protected Function<UnderlyingForm, SubCandidateSet> getRightFunction() {
        return new Function<UnderlyingForm, SubCandidateSet>() {

            @Override
            public SubCandidateSet apply(UnderlyingForm input) {
                // System.out.printf("Getting forms for %s%n", input);
                Set<FormMapping> result = Sets.newHashSet();

                // First get rewrites as strings
                List<EdgeBasedRewrite> rewrites = edgeRuleTransformer.createRewritesForEdgetype(input, Edge.WORD);
                pruneRewrites(rewrites);
                for (EdgeBasedRewrite rewrite : rewrites) {
                    // Get all syllabifications for rewrite
                    Collection<SurfaceForm> resultingSurfaceForms = rewriteToSurfaceForms(rewrite);
                    for (SurfaceForm surfaceForm : resultingSurfaceForms) {
                        FormMapping toAdd = new UfSfMapping(input,
                                surfaceForm, rewrite.getRulesApplied());
                        result.add(toAdd);
                    }

                }

                return SubCandidateSet.of(result);
            }

            private void pruneRewrites(List<EdgeBasedRewrite> rewrites) {
                for (int i = rewrites.size() - 1; i >= 0; i--) {
                    byte[] result = rewrites.get(i).getResult();
                    if (exceedsMaxConsecutiveC(result)) {
                        rewrites.remove(i);
                    }
                }

            }

            private boolean exceedsMaxConsecutiveC(byte[] result) {
                int cCounter = 0;
                for (byte b : result) {
                    if (Sonority.of(b) == Sonority.C) {
                        cCounter++;
                    } else {
                        cCounter = 0;
                    }
                    if (cCounter > MAX_CONSECUTIVE_C) {
                        return true;
                    }
                }
                return false;
            }

        };
    }


    private Collection<SurfaceForm> rewriteToSurfaceForms(
            EdgeBasedRewrite rewrite) {
        Collection<SurfaceForm> result = Sets.newHashSet();
        //System.out.println(operation);
        // First check if legal
        byte[] transformed = rewrite.getResult();
//		if (nGraphMap.isLegal(transformed, MAX_UNKNOWN_SUBSEQUENCE)) {
        List<EdgeIndex> syllabifications = syllabifier.getSyllabifications(transformed);
        for (EdgeIndex edgeIndex : syllabifications) {
            result.add(SurfaceForm.createInstance(transformed, edgeIndex));
        }
//		}
        return result;
    }

}
