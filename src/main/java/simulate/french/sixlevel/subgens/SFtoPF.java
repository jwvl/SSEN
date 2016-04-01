/**
 *
 */
package simulate.french.sixlevel.subgens;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import forms.phon.flat.PhoneticForm;
import forms.phon.flat.SurfaceForm;
import forms.phon.syllable.CachingSimpleSyllabifier;
import forms.phon.syllable.ISyllabifier;
import forms.primitives.boundary.Edge;
import forms.primitives.boundary.EdgeIndex;
import forms.primitives.segment.Phone;
import forms.primitives.segment.PhoneSubForm;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import gen.mapping.specific.SfPfMapping;
import gen.rule.edgebased.EdgeBasedRewrite;
import gen.rule.edgebased.EdgeBasedRule;
import gen.rule.edgebased.EdgeBasedRuleBuilder;
import gen.rule.edgebased.EdgeRuleTransformer;
import gen.subgen.Reversible;
import gen.subgen.SubGen;
import grammar.levels.predefined.BiPhonSix;
import simulate.french.sixlevel.constraints.factories.ArticulatoryConstraintFactory;
import simulate.french.sixlevel.constraints.factories.CueConstraintFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date May 19, 2015
 */
public class SFtoPF extends SubGen<SurfaceForm, PhoneticForm> implements
        Reversible<SurfaceForm, PhoneticForm> {

    EdgeRuleTransformer transformer;
    private Multimap<PhoneticForm, SurfaceForm> reverseMappings;

    public SFtoPF(List<EdgeBasedRule> rules) {
        super(BiPhonSix.getSurfaceFormLevel(), BiPhonSix.getPhoneticLevel());
        this.transformer = EdgeRuleTransformer.createFromRules(rules);
        reverseMappings = HashMultimap.create();
        addConstraintFactory(new CueConstraintFactory());
        addConstraintFactory(ArticulatoryConstraintFactory.createFromPhones(Phone.getInstance('ə')));
    }

    /**
     *
     */
    public SFtoPF() {
        this(createSchwaRules());
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.SubGen#getRightFunction()
     */
    @Override
    protected Function<SurfaceForm, SubCandidateSet> getRightFunction() {
        return new Function<SurfaceForm, SubCandidateSet>() {
            public SubCandidateSet apply(SurfaceForm input) {
                return getFormMappings(input);
            }

        };
    }

    private SubCandidateSet getFormMappings(SurfaceForm input) {
        Set<FormMapping> result = Sets.newHashSet();
        List<EdgeBasedRewrite> rewrites = transformer.createRewritesForEdgetype(input, Edge.SYLLABLE);
        for (EdgeBasedRewrite edgeBasedRewrite : rewrites) {
            byte[] asBytes = edgeBasedRewrite.getResult();
            PhoneticForm resultingForm = PhoneticForm
                    .fromPhoneSubForm(PhoneSubForm.createFromByteArray(asBytes));
            FormMapping toAdd = new SfPfMapping(input, resultingForm, edgeBasedRewrite.getRulesApplied());
            result.add(toAdd);
        }
        return SubCandidateSet.of(result);
    }

    /**
     * @return
     */
    private static List<EdgeBasedRule> createSchwaRules() {
        List<EdgeBasedRule> result = Lists.newArrayList();
        result.addAll(EdgeBasedRuleBuilder.fromString("ə. → ∅ / __|V", Edge.SYLLABLE));
        result.addAll(EdgeBasedRuleBuilder.fromString("∅. → ə / __|C", Edge.SYLLABLE));
        return result;
    }

    public Collection<SurfaceForm> generateLeft(PhoneticForm form) {
        if (reverseMappings.containsKey(form))
            return (reverseMappings.get(form));
        ISyllabifier syllabifier = new CachingSimpleSyllabifier();
        List<EdgeBasedRule> rules = transformer.getRules();
        List<EdgeBasedRule> reversedEdgeless = new ArrayList<EdgeBasedRule>(rules.size());
        for (EdgeBasedRule edgeBasedRule : rules) {
            reversedEdgeless.add(edgeBasedRule.getReverse().getEdgeless());
        }
        EdgeRuleTransformer edgeRuleTransformer = EdgeRuleTransformer.createFromRules(reversedEdgeless);
        List<EdgeBasedRewrite> rewrites = edgeRuleTransformer.createEdgelessRewrites(form);
        Set<SurfaceForm> result = Sets.newHashSet();
        for (EdgeBasedRewrite rewrite : rewrites) {
            byte[] transformed = rewrite.getResult();
            List<EdgeIndex> syllabifications = syllabifier.getSyllabifications(transformed);
            for (EdgeIndex edgeIndex : syllabifications) {
                result.add(SurfaceForm.createInstance(transformed, edgeIndex));
            }
        }
        reverseMappings.putAll(form, result);
        return result;
    }

}
