/**
 *
 */
package gen.rule.string;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jwvl
 * @date Jul 17, 2015
 */
public class SimpleStringRewriteFactory {
    private List<StringRewriteRule> order;
    private List<SimpleStringRewriteOperation> outputs;
    private StringRewriteRule DEFAULT_RULE;
    private SimpleStringRewriteOperation initialOperation;
    private boolean addsDefaultRules = false;

    private SimpleStringRewriteFactory(StringRewritable originalForm,
                                       Collection<StringRewriteRule> rules) {
        initialOperation = new SimpleStringRewriteOperation(originalForm);

        this.order = Lists.newArrayList(rules);
        boolean foundMatch;
        // We begin by removing unmatched rules
        for (int i = this.order.size() - 1; i >= 0; i--) {
            StringRewriteRule rewriteRule = order.get(i);
            Pattern pattern = rewriteRule.getPattern();
            Matcher testMatcher = pattern.matcher(originalForm
                    .toRewritableString());
            foundMatch = (testMatcher.find());
            if (!foundMatch) {
                order.remove(i);
            }
        }
        // We add a final rule which simply concatenates!
        DEFAULT_RULE = createDefaultRule(originalForm);
        order.add(DEFAULT_RULE);
        outputs = Lists.newArrayList();
    }

    public static SimpleStringRewriteFactory createInstance(
            StringRewritable originalString,
            Collection<StringRewriteRule> rules) {
        return new SimpleStringRewriteFactory(originalString, rules);
    }

    public void init() {
        findResultsRecursive(initialOperation);
    }

    private void findResultsRecursive(SimpleStringRewriteOperation building) {
        Matcher matcher;
        boolean anyMatchesFound = false;
        for (StringRewriteRule grr : order) {
            matcher = grr.getMatcher(building.getCurrentState());
            if (matcher.find()) {
                anyMatchesFound = true;
                SimpleStringRewriteOperation copy = building.copy();
                copy.applyRule(grr, matcher);
                if (grr != DEFAULT_RULE || addsDefaultRules) {
                    copy.addRule(grr);
                }
                findResultsRecursive(copy);
            }
        }
        if (!anyMatchesFound) {
            outputs.add(building);
        }
    }

    public List<SimpleStringRewriteOperation> getResults() {
        return outputs;
    }

    public static StringRewriteRule createDefaultRule(StringRewritable sr) {
        String focus = String.valueOf(sr.getSubformConcatenator());
        String target = "";
        return SimpleStringRewriteRule.createSimple(focus, target);
    }
}
