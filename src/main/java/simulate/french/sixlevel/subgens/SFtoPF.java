/**
 *
 */
package simulate.french.sixlevel.subgens;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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
import simulate.french.sixlevel.constraints.factories.SimplePhoneCombinationConstraintFactory;

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

    private final static Config config = ConfigFactory.load();

    EdgeRuleTransformer transformer;
    private Multimap<PhoneticForm, SurfaceForm> reverseMappings;

    public SFtoPF(List<EdgeBasedRule> rules) {
        super(BiPhonSix.getSurfaceFormLevel(), BiPhonSix.getPhoneticLevel());
        rules.addAll(addAbstractRewrites());
        this.transformer = EdgeRuleTransformer.createFromRules(rules);
        reverseMappings = HashMultimap.create();
        List<Phone> cuePhones = Lists.newArrayList(Phone.getInstance('ə'));
        addAbstractPhones(cuePhones);
        addConstraintFactory(new CueConstraintFactory());
        addConstraintFactory(ArticulatoryConstraintFactory.createFromPhones(cuePhones));
        SimplePhoneCombinationConstraintFactory combinationConstraintFactory = new SimplePhoneCombinationConstraintFactory();
        combinationConstraintFactory.addFromString("VV","CC");
        addConstraintFactory(combinationConstraintFactory);
       // addConstraintFactory(new SonorityCombinationConstraintFactory(3));
    }

    private List<EdgeBasedRule> addAbstractRewrites() {
        List<EdgeBasedRule> result = new ArrayList<>();
        if (config.getBoolean("gen.abstractEnabled")) {
            List<String> strings = config.getStringList("gen.abstractPhonemes");
            for (String string: strings) {
                String[] parts = string.split("~");
                String realPhoneme = parts[0];
                String archiPhoneme = parts[1];
                String deletionRight = String.format(".%s → ∅ / __", archiPhoneme);
                String deletionLeft = String.format("%s. → ∅ / __", archiPhoneme);
                String realizationRight = String.format(".%s → %s / __", archiPhoneme, realPhoneme);
                String realizationLeft = String.format("%s. → %s / __", archiPhoneme, realPhoneme);

                result.addAll(EdgeBasedRuleBuilder.fromString(deletionLeft, Edge.SYLLABLE));
                result.addAll(EdgeBasedRuleBuilder.fromString(deletionRight, Edge.SYLLABLE));
                result.addAll(EdgeBasedRuleBuilder.fromString(realizationRight, Edge.SYLLABLE));
                result.addAll(EdgeBasedRuleBuilder.fromString(realizationLeft, Edge.SYLLABLE));

            }
        }
        return result;
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
        if (config.getBoolean("gen.schwaDeletionOnSF")) {
            result.addAll(EdgeBasedRuleBuilder.fromString("ə. → ∅ / __", Edge.SYLLABLE));
        }
        if (config.getBoolean("gen.schwaInsertionOnSF")) {
             result.addAll(EdgeBasedRuleBuilder.fromString("∅. → ə / __|C", Edge.SYLLABLE));
        }
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

    private void addAbstractPhones(List<Phone> cuePhones) {
        Config config = ConfigFactory.load();
        if (config.getBoolean("gen.abstractEnabled")) {
            List<String> strings = config.getStringList("gen.abstractPhonemes");
            for (String string: strings) {
                String[] parts = string.split("~");
                cuePhones.add(Phone.getInstance(parts[1].charAt(0)));
            }
        }
    }

//    private List<RewriteRule> deleteLiaisonConsonants() {
//        List<RewriteRule> rules = Lists.newArrayList();
//        result.addAll(EdgeBasedRuleBuilder.fromString("ə. → ∅ / __", Edge.SYLLABLE));
//    }

}
