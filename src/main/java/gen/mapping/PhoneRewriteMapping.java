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
public abstract class PhoneRewriteMapping<F extends Form, G extends Form> extends FormMapping {
    private final List<PhoneTransform> rewrites;

    public PhoneRewriteMapping(F before, G after, Collection<RewriteRule> rules) {
        super(before, after);
        rewrites = new ArrayList<PhoneTransform>(rules.size() * 3);
        for (RewriteRule rule : rules) {
            rewrites.addAll(rule.getTransforms());
        }
    }

    public List<PhoneTransform> getRewrites() {
        return rewrites;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PhoneRewriteMapping<?, ?> that = (PhoneRewriteMapping<?, ?>) o;

        return rewrites != null ? rewrites.equals(that.rewrites) : that.rewrites == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (rewrites != null ? rewrites.hashCode() : 0);
        return result;
    }
}
