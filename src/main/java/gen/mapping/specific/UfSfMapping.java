/**
 *
 */
package gen.mapping.specific;

import forms.phon.flat.SurfaceForm;
import forms.phon.flat.UnderlyingForm;
import gen.mapping.PhoneRewriteMapping;
import gen.rule.RewriteRule;

import java.util.Collection;

/**
 * @author jwvl
 * @date Sep 1, 2015
 */
public class UfSfMapping extends PhoneRewriteMapping<UnderlyingForm, SurfaceForm> {

    /**
     * @param before
     * @param after
     * @param operation
     */
    public UfSfMapping(UnderlyingForm before, SurfaceForm after, Collection<RewriteRule> operation) {
        super(before, after, operation);
    }

}
