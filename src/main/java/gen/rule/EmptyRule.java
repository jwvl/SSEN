/**
 *
 */
package gen.rule;

import forms.phon.PhoneTransform;

import java.util.Collections;
import java.util.List;

/**
 * @author jwvl
 * @date 20/02/2016
 */
public class EmptyRule implements RewriteRule {
    public final static RewriteRule INSTANCE = new EmptyRule();

    @Override
    public List<PhoneTransform> getTransforms() {
        return Collections.emptyList();
    }

}
