/**
 *
 */
package gen.rule.edgebased;

import forms.primitives.segment.Phone;
import gen.rule.EmptyRule;
import gen.rule.RewriteRule;
import util.string.CollectionPrinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jwvl
 * @date 20/02/2016
 */
public class EdgeBasedRewrite {
    private final byte[] outcome;
    private final List<RewriteRule> rulesApplied;

    /**
     * @param outcome
     * @param rulesApplied
     */
    private EdgeBasedRewrite(byte[] outcome, List<RewriteRule> rulesApplied) {
        this.outcome = outcome;
        this.rulesApplied = rulesApplied;
    }

    /**
     * @param ruleOrder
     * @param original
     * @param indices
     * @return
     */
    public static EdgeBasedRewrite createFromRuleOrder(List<RewriteRule> ruleOrder, byte[] original,
                                                       int[] indices) {
        int indexOffset = 0;
        List<RewriteRule> rulesApplied = new ArrayList<RewriteRule>(ruleOrder.size());

        for (int i = 0; i < indices.length; i++) {

            RewriteRule rule = ruleOrder.get(i);
            if (rule == EmptyRule.INSTANCE) {
                //
            } else {
                EdgeBasedRule edgeBasedRule = (EdgeBasedRule) rule;
                int actualIndex = indices[i] + indexOffset;
                original = edgeBasedRule.apply(original, actualIndex);
                indexOffset += edgeBasedRule.getOffset();
                rulesApplied.add(rule);
            }
        }
        return new EdgeBasedRewrite(original, rulesApplied);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(outcome);
        result = prime * result + ((rulesApplied == null) ? 0 : rulesApplied.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EdgeBasedRewrite))
            return false;
        EdgeBasedRewrite other = (EdgeBasedRewrite) obj;
        if (!Arrays.equals(outcome, other.outcome))
            return false;
        if (rulesApplied == null) {
            if (other.rulesApplied != null)
                return false;
        } else if (!rulesApplied.equals(other.rulesApplied))
            return false;
        return true;
    }

    public String toString() {
        return Phone.decode(outcome) + " " + CollectionPrinter.collectionToString(rulesApplied, ",");
    }

    public byte[] getResult() {
        return outcome;
    }

    public List<RewriteRule> getRulesApplied() {
        return rulesApplied;
    }

}
