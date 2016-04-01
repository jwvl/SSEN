/**
 *
 */
package gen.rule.bytes;

import gen.rule.string.StringRewriteRule;

import java.util.Collection;

/**
 * @author jwvl
 * @date Sep 1, 2015
 */
public class SimpleByteRewriteRuleFactory {

    public static SimpleByteRewriteRule fromStringRewriteRule(StringRewriteRule stringRewriteRule) {

        String leftContext = "";
        String rightContext = "";
        String focus = "";
        String target = stringRewriteRule.getTarget();
        Collection<String> leftContexts = stringRewriteRule.getLeftContexts();
        if (!leftContexts.isEmpty()) {
            leftContext += leftContexts.iterator().next();
        }
        Collection<String> rightContexts = stringRewriteRule.getRightContexts();
        if (!rightContexts.isEmpty()) {
            rightContext += rightContexts.iterator().next();
        }
        Collection<String> foci = stringRewriteRule.getFocus();
        if (!foci.isEmpty()) {
            focus += foci.iterator().next();
        }
        String beforeString = leftContext + focus + rightContext;
        String afterString = rightContext + target + rightContext;
        return SimpleByteRewriteRule.createFromStrings(beforeString, afterString);

    }

}
