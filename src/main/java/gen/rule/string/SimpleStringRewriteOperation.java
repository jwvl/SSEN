/**
 *
 */
package gen.rule.string;

import com.google.common.collect.Lists;
import gen.rule.RewriteRule;

import java.util.Collection;
import java.util.regex.Matcher;

/**
 * @author jwvl
 * @date Jul 18, 2015
 */
public class SimpleStringRewriteOperation {

    private final Collection<RewriteRule> rulesApplied;
    private final StringRewritable original;
    private StringBuilder currentState;

    /**
     * @param rulesApplied
     * @param original
     */
    public SimpleStringRewriteOperation(StringRewritable original) {
        this.rulesApplied = Lists.newArrayList();
        this.original = original;
        this.currentState = new StringBuilder(original.toRewritableString());
    }

    /**
     * @param rulesApplied
     * @param original
     */
    private SimpleStringRewriteOperation(SimpleStringRewriteOperation toCopy) {
        this.rulesApplied = Lists.newArrayList(toCopy.getRulesApplied());
        this.original = toCopy.original;
        this.currentState = new StringBuilder(toCopy.currentState);
    }

    public SimpleStringRewriteOperation copy() {
        SimpleStringRewriteOperation result = new SimpleStringRewriteOperation(
                this);
        return result;
    }

    public void applyRule(StringRewriteRule stringRewriteRule, Matcher matcher) {
        stringRewriteRule.apply(currentState, matcher);
    }

    public void addRule(StringRewriteRule stringRewriteRule) {
        rulesApplied.add(stringRewriteRule);
    }

    public String getResult() {
        return currentState.toString();
    }

    public Collection<RewriteRule> getRulesApplied() {
        return rulesApplied;
    }

    /**
     * @return the original
     */
    public StringRewritable getOriginal() {
        return original;
    }

    public StringBuilder getCurrentState() {
        return currentState;
    }

}
