/**
 *
 */
package gen.rule.string;

import gen.rule.RewriteRule;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author jwvl
 * @date Jul 17, 2015
 */
public interface StringRewriteRule extends RewriteRule {

    void apply(StringBuilder building, Matcher matcher);

    Pattern getPattern();

    Matcher getMatcher(CharSequence cs);

    String getSpeStyleString();

    Collection<String> getLeftContexts();

    Collection<String> getRightContexts();

    Collection<String> getFocus();

    String getTarget();


}
