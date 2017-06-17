/**
 *
 */
package simulate.french.sixlevel.helpers;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import forms.primitives.boundary.Edge;
import gen.rule.edgebased.EdgeBasedRule;
import gen.rule.edgebased.EdgeBasedRuleBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jwvl
 * @date 25/09/2015
 * Contains the predefined SF->PF phonological rules for the simulations of Chapter 5.
 */
public class PredefinedArticulatoryRules {
    public static List<EdgeBasedRule> nasalRules = createNasalRules();
    public static final Config config = ConfigFactory.load();
    /**
     * @return
     */
    private static List<EdgeBasedRule> createNasalRules() {
        Config config = ConfigFactory.load();
        List<EdgeBasedRule> result = new ArrayList<EdgeBasedRule>();
        if (config.getBoolean("gen.nasalRulesOnSF")) {
            result.addAll(EdgeBasedRuleBuilder.fromString(".n → ∅ / __", Edge.SYLLABLE));
            result.addAll(EdgeBasedRuleBuilder.fromString("n. → ∅ / __", Edge.SYLLABLE));
        }
        return result;
    }

    private static List<EdgeBasedRule> createLiaisonRules()
    {
        List<EdgeBasedRule> result = new ArrayList<>();
        if (config.getBoolean("gen.deleteliaisonsOnSF")) {
            result.addAll(EdgeBasedRuleBuilder.fromString(".z → ∅ / __", Edge.SYLLABLE));
            result.addAll(EdgeBasedRuleBuilder.fromString("z. → ∅ / __", Edge.SYLLABLE));
            result.addAll(EdgeBasedRuleBuilder.fromString(".t → ∅ / __", Edge.SYLLABLE));
            result.addAll(EdgeBasedRuleBuilder.fromString("t. → ∅ / __", Edge.SYLLABLE));
        }
        if (config.getBoolean("gen.insertionRulesOnUF")) {
            result.addAll(EdgeBasedRuleBuilder.fromString("#∅ → n / |V__", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("∅# → n / __|V", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("#∅ → z / |V__", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("∅# → z / __|V", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("#∅ → t / |V__", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("∅# → t / __|V", Edge.WORD));
        }
        result.addAll(EdgeBasedRuleBuilder.fromString("Q# → ɔn / __", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("ɔn# → Q / __", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("3# → ɛn / __", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("ɛn# → 3 / __", Edge.WORD));

        if (config.getBoolean("gen.schwaInsertionOnUF")) {
            result.addAll(EdgeBasedRuleBuilder.fromString("∅# → ə / |C__|C", Edge.MORPHEME));
        }
        return result;

    }

}
