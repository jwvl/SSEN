/**
 *
 */
package gen.rule.bytes;

import com.google.common.collect.Lists;
import forms.primitives.segment.Phone;
import gen.rule.RewriteRule;
import util.arrays.ByteArrayUtils;
import util.arrays.ByteBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author jwvl
 * @date Sep 1, 2015
 */
public class SimpleByteRewriter {

    private final byte[] original;
    private final SimpleByteRewriteRule[] rules;
    private List<ByteRewriteOperation> rewrites;
    private static byte[] appendExceptions = createAppendExceptions();

    /**
     * @param original
     * @param rules
     */
    private SimpleByteRewriter(byte[] original, SimpleByteRewriteRule[] rules) {
        this.original = original;
        this.rules = rules;
        this.rewrites = Lists.newArrayList();
    }

    public static SimpleByteRewriter create(byte[] original, SimpleByteRewriteRule... rules) {
        SimpleByteRewriteRule[] necessary = deleteUnnessecaryRules(original, rules);
        return new SimpleByteRewriter(original, necessary);
    }

    public static List<ByteRewriteOperation> getRewrites(byte[] original, SimpleByteRewriteRule... rules) {
        SimpleByteRewriter created = create(original, rules);
        created.makeRewrites();
        return created.rewrites;
    }

    /**
     * @param original2
     * @param rules2
     */
    private static SimpleByteRewriteRule[] deleteUnnessecaryRules(byte[] original,
                                                                  SimpleByteRewriteRule[] rules) {
        List<SimpleByteRewriteRule> necessaryRuleBuilder = new ArrayList<SimpleByteRewriteRule>(rules.length);
        for (SimpleByteRewriteRule rule : rules) {
            if (ByteArrayUtils.indexOf(original, rule.getFocus()) >= 0) {
                necessaryRuleBuilder.add(rule);
            }
        }
        SimpleByteRewriteRule[] result = new SimpleByteRewriteRule[necessaryRuleBuilder.size()];
        return necessaryRuleBuilder.toArray(result);
    }

    private void makeRewrites() {
        ByteBuilder emptyBuilder = new ByteBuilder(original.length * 2);
        makeRewrites(emptyBuilder, 0, new int[original.length]);
    }

    private void makeRewrites(ByteBuilder bb, int idx, int[] ruleIndexArray) {
        if (idx >= original.length) {
            rewrites.add(createRewrite(bb.build(), ruleIndexArray));
        } else {
            boolean matchFound = false;
            // cycle through rules
            for (int i = 0; i < rules.length; i++) {
                SimpleByteRewriteRule rule = rules[i];
                if (matches(rule.getFocus(), idx)) {
                    ByteBuilder bbCopy = bb.copy();
                    bbCopy.append(rule.getTarget());
                    int[] newArray = addToRuleArray(ruleIndexArray, i, idx);
                    makeRewrites(bbCopy, idx + rule.getFocus().length, newArray);
                    // matchFound = true;
                }

            }
            bb.appendExcept(original[idx], appendExceptions);
            makeRewrites(bb, idx + 1, ruleIndexArray);

        }
    }

    /**
     * @param build
     * @param ruleIndexArray
     * @return
     */
    private ByteRewriteOperation createRewrite(byte[] build, int[] ruleIndexArray) {
        List<RewriteRule> ruleList = new ArrayList<RewriteRule>(original.length);
        for (int i : ruleIndexArray) {
            if (i > 0) {
                RewriteRule toAdd = rules[i - 1];
                ruleList.add(toAdd);

            }
        }
        return ByteRewriteOperation.createInstance(build, ruleList);
    }

    private boolean matches(byte[] sequence, int atIndex) {
        if (atIndex > original.length - sequence.length) {
            return false;
        } else {
            for (int i = 0; i < sequence.length; i++) {
                if (original[i + atIndex] != sequence[i])
                    return false;
            }
        }
//		System.out.printf("Match: found %s in %s[%d]%n", Phone.decode(sequence), Phone.decode(original),
//				atIndex);
        return true;
    }

    private int[] addToRuleArray(int[] toCopy, int ruleApplied, int atIndex) {
        // // Don't include default rule (last in rules list)
        // if (rules[ruleApplied].equals(defaultRule)) {
        // return toCopy;
        // }
        int[] result = Arrays.copyOf(toCopy, original.length);
        result[atIndex] = ruleApplied + 1;
        return result;
    }

    /**
     * @return
     */
    private static byte[] createAppendExceptions() {
        char[] asChars = {'.', '+', '/', '[', ']', '-', '∅'};
        byte[] result = new byte[asChars.length];
        for (int i = 0; i < asChars.length; i++) {
            result[i] = Phone.getInstance(asChars[i]).getId();
        }
        return result;
    }

}
