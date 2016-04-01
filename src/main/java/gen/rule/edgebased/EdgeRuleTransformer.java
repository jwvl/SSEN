/**
 *
 */
package gen.rule.edgebased;

import com.google.common.collect.Sets;
import forms.phon.flat.PhoneSequence;
import forms.primitives.boundary.Edge;
import forms.primitives.boundary.EdgeIndex;
import gen.rule.EmptyRule;
import gen.rule.RewriteRule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date 20/02/2016
 */
public class EdgeRuleTransformer {
    private final List<EdgeBasedRule> rules;

    /**
     * @param rules
     */
    private EdgeRuleTransformer(List<EdgeBasedRule> rules) {
        this.rules = rules;
    }

    public static EdgeRuleTransformer createFromRules(List<EdgeBasedRule> rules) {
        return new EdgeRuleTransformer(rules);
    }

    public List<EdgeBasedRewrite> createRewritesForEdgetype(PhoneSequence original, Edge edge) {
        EdgeIndex edgeIndex = original.getBoundaries();
        int[] indices = edgeIndex.getIndices(edge);
        return createRewritesForIndices(original, indices);
    }

    private List<EdgeBasedRewrite> createRewritesForIndices(PhoneSequence original, int[] indices) {
        List<Set<RewriteRule>> ruleSetList = new ArrayList<Set<RewriteRule>>(indices.length);
        byte[] asBytes = original.getByteArray();
        for (int index : indices) {
            ruleSetList.add(findRulesApplying(index, asBytes));
        }
        Set<List<RewriteRule>> cartesianProduct = Sets.cartesianProduct(ruleSetList);
        List<EdgeBasedRewrite> result = createResultsFromCartesianProduct(cartesianProduct, asBytes, indices);
        return result;
    }

    public List<EdgeBasedRewrite> createEdgelessRewrites(PhoneSequence original) {
        int[] indices = new int[original.size() + 1];
        for (int i = 0; i <= original.size(); i++) {
            indices[i] = i;
        }
        return createRewritesForIndices(original, indices);
    }

    /**
     * @param cartesianProduct
     * @return
     */
    private List<EdgeBasedRewrite> createResultsFromCartesianProduct(Set<List<RewriteRule>> cartesianProduct, byte[] original, int[] indices) {
        List<EdgeBasedRewrite> result = new ArrayList<EdgeBasedRewrite>(cartesianProduct.size());
        for (List<RewriteRule> ruleOrder : cartesianProduct) {
            EdgeBasedRewrite rewrite = EdgeBasedRewrite.createFromRuleOrder(ruleOrder, original, indices);
            result.add(rewrite);
        }
        return result;
    }

    private Set<RewriteRule> findRulesApplying(int index, byte[] toSearch) {
        Set<RewriteRule> result = new HashSet<RewriteRule>();
        for (EdgeBasedRule rule : rules) {
            if (rule.ruleMatches(toSearch, index)) {
                result.add(rule);
            }
        }
        result.add(EmptyRule.INSTANCE);
        return result;
    }

    public List<EdgeBasedRule> getRules() {
        return rules;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rules == null) ? 0 : rules.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EdgeRuleTransformer))
            return false;
        EdgeRuleTransformer other = (EdgeRuleTransformer) obj;
        if (rules == null) {
            if (other.rules != null)
                return false;
        } else if (!rules.equals(other.rules))
            return false;
        return true;
    }


}
