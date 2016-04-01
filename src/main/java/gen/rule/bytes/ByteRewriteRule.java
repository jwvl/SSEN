/**
 *
 */
package gen.rule.bytes;

import forms.phon.PhoneTransform;
import forms.primitives.segment.PhoneClass;
import gen.rule.RewriteRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jwvl
 * @date 26/09/2015
 */
public class ByteRewriteRule implements RewriteRule {

    private final List<PhoneTransform> transform;
    private PhoneClass leftContext;
    private PhoneClass rightContext;
    private byte focus;
    private byte target;

    @Override
    public List<PhoneTransform> getTransforms() {
        return transform;
    }

    /**
     * @param leftContext
     * @param rightContext
     * @param focus
     * @param target
     */
    public ByteRewriteRule(PhoneClass leftContext, PhoneClass rightContext, byte focus, byte target) {
        this.leftContext = leftContext;
        this.rightContext = rightContext;
        this.focus = focus;
        this.target = target;
        PhoneTransform transform = PhoneTransform.getInstance(new byte[]{focus, target});
        System.out.println("Created transform: " + transform);
        this.transform = Collections.singletonList(transform);
    }

    public List<SimpleByteRewriteRule> toSimpleRules() {
        List<SimpleByteRewriteRule> result = new ArrayList<SimpleByteRewriteRule>(leftContext.size() * rightContext.size());
        for (byte leftByte : leftContext.getContents()) {
            for (byte rightByte : rightContext.getContents()) {
                byte[] before = new byte[]{leftByte, focus, rightByte};
                byte[] after = new byte[]{leftByte, target, rightByte};
                SimpleByteRewriteRule rule = SimpleByteRewriteRule.createFromBytes(before, after);
                rule.setTransforms(transform);
                result.add(rule);
            }
        }

        return result;
    }


}
