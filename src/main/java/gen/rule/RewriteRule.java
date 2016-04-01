/**
 *
 */
package gen.rule;

import forms.phon.PhoneTransform;

import java.util.List;

/**
 * This interface encodes the notion of a generative-style 'rewrite rule'. It is
 * used to generate (GEN-like) candidate (paths) in a BiPhon grammar.
 *
 * @author jwvl
 * @date Aug 20, 2014
 */
public interface RewriteRule {
    List<PhoneTransform> getTransforms();
}
