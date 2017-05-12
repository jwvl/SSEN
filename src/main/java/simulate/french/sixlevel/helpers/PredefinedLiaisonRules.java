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
        if (config.getBoolean("gen.deletionRulesOnUF")) {
            result.addAll(EdgeBasedRuleBuilder.fromString("#z → ∅ / __", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("z# → ∅ / __", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("#n → ∅ / __", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("n# → ∅ / __", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("#t → ∅ / __", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("t# → ∅ / __", Edge.WORD));
//            result.addAll(EdgeBasedRuleBuilder.fromString("#z → ∅ / |C__", Edge.WORD));
//            result.addAll(EdgeBasedRuleBuilder.fromString("z# → ∅ / __|C", Edge.WORD));
//            result.addAll(EdgeBasedRuleBuilder.fromString("#n → ∅ / |C__", Edge.WORD));
//            result.addAll(EdgeBasedRuleBuilder.fromString("n# → ∅ / __|C", Edge.WORD));
//            result.addAll(EdgeBasedRuleBuilder.fromString("#t → ∅ / |C__", Edge.WORD));
//            result.addAll(EdgeBasedRuleBuilder.fromString("t# → ∅ / __|C", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("ə# → ∅ / __", Edge.WORD));
            result.addAll(EdgeBasedRuleBuilder.fromString("#ə → ∅ / __", Edge.WORD));

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

        if (config.getBoolean("gen.abstractEnabled")) {
            List<String> strings = config.getStringList("gen.abstractPhonemes");
            for (String string: strings) {
                String[] parts = string.split("~");
                String realPhoneme = parts[0];
                String archiPhoneme = parts[1];
                String deletionRight = String.format("#%s → ∅ / __", archiPhoneme);
                String deletionLeft = String.format("%s# → ∅ / __", archiPhoneme);
                String realizationRight = String.format("#%s → %s / __", archiPhoneme, realPhoneme);
                String realizationLeft = String.format("%s# → %s / __", archiPhoneme, realPhoneme);

                result.addAll(EdgeBasedRuleBuilder.fromString(deletionLeft, Edge.WORD));
                result.addAll(EdgeBasedRuleBuilder.fromString(deletionRight, Edge.WORD));
                result.addAll(EdgeBasedRuleBuilder.fromString(realizationRight, Edge.WORD));
                result.addAll(EdgeBasedRuleBuilder.fromString(realizationLeft, Edge.WORD));

            }
        }

        return result;
    }

}
