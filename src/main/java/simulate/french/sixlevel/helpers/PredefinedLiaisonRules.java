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
 * Contains the predefined UF->SF phonological rules for the simulations of Chapter 5.
 */
public class PredefinedLiaisonRules {
    public static List<EdgeBasedRule> edgeRules = createEdgeRules();

    /**
     * @return
     */
    private static List<EdgeBasedRule> createEdgeRules() {
        Config config = ConfigFactory.load();
        List<EdgeBasedRule> result = new ArrayList<EdgeBasedRule>();
        result.addAll(EdgeBasedRuleBuilder.fromString("#z → ∅ / |C__", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("z# → ∅ / __|C", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("#n → ∅ / |C__", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("n# → ∅ / __|C", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("#t → ∅ / |C__", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("t# → ∅ / __|C", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("#∅ → n / |V__", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("∅# → n / __|V", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("#∅ → z / |V__", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("∅# → z / __|V", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("#∅ → t / |V__", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("∅# → t / __|V", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("Q# → ɔn / __|V", Edge.WORD));
        result.addAll(EdgeBasedRuleBuilder.fromString("ɔn# → Q / __|C", Edge.WORD));
        if (config.getBoolean("gen.schwaRulesOnUF")) {
            result.addAll(EdgeBasedRuleBuilder.fromString("ə# → ∅ / __", Edge.MORPHEME));
            result.addAll(EdgeBasedRuleBuilder.fromString("∅# → ə / |C__|C", Edge.MORPHEME));
        }

        return result;
    }

}
