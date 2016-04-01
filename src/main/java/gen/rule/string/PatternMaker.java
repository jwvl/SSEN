/**
 *
 */
package gen.rule.string;

import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * @author jwvl
 * @date Jul 12, 2015
 * Creates a Pattern from collections of Strings representing the left & right
 * contexts and focus of a phonological rule.
 */
public class PatternMaker {
    private Collection<String> leftContexts;
    private Collection<String> rightContexts;
    private Collection<String> focus;

    /**
     * @param leftContext
     * @param rightContext
     * @param focus
     */
    private PatternMaker(Collection<String> leftContext,
                         Collection<String> rightContext, Collection<String> focus) {
        this.leftContexts = leftContext;
        this.rightContexts = rightContext;
        this.focus = focus;
    }

    public Pattern getPattern() {
        StringBuilder result = new StringBuilder();
        result.append(getPatternString(leftContexts));
        result.append(getPatternString(focus));
        result.append(getPatternString(rightContexts));
        return Pattern.compile(result.toString());
    }

    private static String getPatternString(Collection<String> strings) {
        StringBuilder stringResult = new StringBuilder('(');
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()) {
            stringResult.append(iterator.next());
            if (iterator.hasNext())
                stringResult.append("|");
        }
        stringResult.append(')');
        return stringResult.toString();
    }

}
