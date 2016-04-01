/**
 *
 */
package simulate.french.sixlevel.helpers;

import forms.primitives.boundary.Edge;
import gen.rule.edgebased.EdgeBasedRule;
import gen.rule.edgebased.EdgeBasedRuleBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jwvl
 * @date 25/09/2015
 */
public class PredefinedLiaisonRules {
    public static List<EdgeBasedRule> edgeRules = createEdgeRules();

    /**
     * @return
     */
    private static List<EdgeBasedRule> createEdgeRules() {
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

        return result;
    }

}
