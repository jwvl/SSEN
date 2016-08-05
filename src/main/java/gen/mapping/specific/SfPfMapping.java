/**
 *
 */
package gen.mapping.specific;

import forms.phon.flat.PhoneticForm;
import forms.phon.flat.SurfaceForm;
import gen.mapping.PhoneRewriteMapping;
import gen.rule.RewriteRule;
import gen.rule.bytes.ByteRewriteOperation;
import gen.rule.string.SimpleStringRewriteOperation;

import java.util.Collection;

/**
 * @author jwvl
 * @date Jul 25, 2015
 */
public class SfPfMapping extends PhoneRewriteMapping {

    public SfPfMapping(SurfaceForm before, PhoneticForm after, Collection<RewriteRule> rules) {
        super(before, after, rules);
    }

    /**
     * @param before
     * @param after
     * @param operation
     */
    public SfPfMapping(SurfaceForm before, PhoneticForm after,
                       SimpleStringRewriteOperation operation) {
        super(before, after, operation.getRulesApplied());
    }

    /**
     * @param before
     * @param after
     * @param operation
     */
    public SfPfMapping(SurfaceForm before, PhoneticForm after,
                       ByteRewriteOperation operation) {
        super(before, after, operation.getRulesApplied());
    }


}
