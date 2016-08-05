/**
 *
 */
package gen.mapping;

import forms.Form;
import forms.phon.PhoneTransform;
import gen.rule.RewriteRule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author jwvl
 * @date Jul 23, 2015
 */
public abstract class PhoneRewriteMapping extends FormMapping {
    private final ArrayList<PhoneTransform> rewrites;

    public PhoneRewriteMapping(Form before, Form after, Collection<RewriteRule> rules) {
        super(before, after);
        rewrites = new ArrayList<PhoneTransform>(rules.size() * 3);
        for (RewriteRule rule : rules) {
            rewrites.addAll(rule.getTransforms());
        }
        rewrites.trimToSize();
    }

    public List<PhoneTransform> getRewrites() {
        return rewrites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PhoneRewriteMapping that = (PhoneRewriteMapping) o;

        return rewrites != null ? rewrites.equals(that.rewrites) : that.rewrites == null;

    }
}
