/**
 *
 */
package gen.rule.bytes;

import com.google.common.collect.Lists;
import util.arrays.ByteBuilder;

import java.util.Iterator;
import java.util.List;

/**
 * @author jwvl
 * @date Aug 29, 2015
 */
public class SimpleByteRewriteRuleOrder {
    private final List<SimpleByteRewriteRule> rules;

    /**
     * @param rules
     */
    private SimpleByteRewriteRuleOrder(List<SimpleByteRewriteRule> rules) {
        this.rules = rules;
    }

    public static SimpleByteRewriteRuleOrder createFromRules(SimpleByteRewriteRule... input) {
        return new SimpleByteRewriteRuleOrder(Lists.newArrayList(input));
    }

    public byte[] apply(byte[] original) {
        ByteBuilder builder = new ByteBuilder(original.length * 2);
        int originalIndex = 0;
        Iterator<SimpleByteRewriteRule> ruleIterator = rules.iterator();
        int nextFocus;
        SimpleByteRewriteRule current = null;
        if (ruleIterator.hasNext()) {
            current = ruleIterator.next();
            nextFocus = current.getNextFocusIndex(original, originalIndex);
        } else {
            nextFocus = original.length;
        }
        while (originalIndex < original.length) {
            // Two options: copy part up till next focus, or replace focus with
            // target
            if (originalIndex == nextFocus) {
                builder.append(current.getTarget());
                originalIndex += current.getFocus().length;
                if (ruleIterator.hasNext()) {
                    // find next focus, or set it to end of original
                    current = ruleIterator.next();
                    nextFocus = current.getNextFocusIndex(original, originalIndex);
                } else {
                    nextFocus = original.length;
                }
            } else {
                builder.copySubset(original, originalIndex, nextFocus);
                originalIndex = nextFocus;
            }

        }
        return builder.build();

    }

}
