/**
 *
 */
package gen.rule.bytes;

import forms.primitives.segment.Phone;
import gen.rule.RewriteRule;

import java.util.Collection;

/**
 * @author jwvl
 * @date Jul 18, 2015
 */
public class ByteRewriteOperation {

    private final Collection<RewriteRule> rulesApplied;
    private final byte[] result;


    /**
     * @param rulesApplied
     * @param original
     */
    private ByteRewriteOperation(Collection<RewriteRule> rulesApplied, byte[] result) {
        this.rulesApplied = rulesApplied;
        this.result = result;
    }

    /**
     * @param build
     * @param list
     * @return
     */
    public static ByteRewriteOperation createInstance(byte[] build, Collection<RewriteRule> list) {
        return new ByteRewriteOperation(list, build);
    }


    public String getResultString() {
        return Phone.decode(result);
    }

    public Collection<RewriteRule> getRulesApplied() {
        return rulesApplied;
    }

    /**
     * @return
     */
    public byte[] getResult() {
        return result;
    }

    public String toString() {
        return rulesApplied.toString() + Phone.decode(result);
    }


}
