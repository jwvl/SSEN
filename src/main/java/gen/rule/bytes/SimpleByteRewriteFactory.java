/**
 *
 */
package gen.rule.bytes;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date Jul 17, 2015
 */
public class SimpleByteRewriteFactory {
    private final List<SimpleByteRewriteRule> order;

    /**
     * @param order
     * @param dEFAULT_RULE
     * @param addsDefaultRules
     */
    private SimpleByteRewriteFactory(Collection<SimpleByteRewriteRule> order) {
        this.order = Lists.newArrayList(order);
    }

    public List<ByteRewriteOperation> getRewrites(byte[] input) {
        SimpleByteRewriteRule[] asArray = new SimpleByteRewriteRule[order.size()];
        order.toArray(asArray);
        return SimpleByteRewriter.getRewrites(input, asArray);
    }

    public static SimpleByteRewriteFactory createInstance(Collection<SimpleByteRewriteRule> rules) {
        return new SimpleByteRewriteFactory(rules);
    }


}
