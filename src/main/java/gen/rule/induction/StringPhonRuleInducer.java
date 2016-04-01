/**
 *
 */
package gen.rule.induction;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import forms.phon.PhoneTransform;
import forms.phon.flat.PhoneSequence;
import forms.primitives.segment.PhoneSubForm;
import gen.rule.edit.SimplePhoneStringDistance;
import gen.rule.string.Side;
import gen.rule.string.SimpleStringRewriteRule;
import gen.rule.string.StringRewriteRule;

import java.util.*;

/**
 * @author jwvl
 * @date Jul 19, 2015
 */
public class StringPhonRuleInducer<S extends PhoneSequence> {
    private String subformString;
    private int maxLengthDifference = 1;

    public StringPhonRuleInducer() {
        List<PhoneSubForm> emptyList = Collections.emptyList();
        this.subformString = "#";
    }

    public Collection<StringRewriteRule> induce(
            Collection<PhoneSubForm> allomorphs) {
        List<PhoneSubForm> asList = Lists.newArrayList(allomorphs);
        Set<StringRewriteRule> result = Sets.newHashSet();
        for (int i = 0; i < asList.size() - 1; i++) {
            for (int j = i + 1; j < asList.size(); j++) {
                PhoneSubForm psf1 = asList.get(i);
                PhoneSubForm psf2 = asList.get(j);
                Side edgeToTransform = edgesAreEqual(psf1, psf2);
                if (areEligibleForTransform(psf1, psf2)
                        && edgeToTransform != Side.EITHER
                        && edgeToTransform != Side.NEITHER) {
                    Collection<StringRewriteRule> transformRules = getTransformRules(
                            psf1, psf2, edgeToTransform);
                    result.addAll(transformRules);
                }
            }
        }
        return result;

    }

    public boolean areEligibleForTransform(PhoneSubForm psf1, PhoneSubForm psf2) {
        return Math.abs(psf1.size() - psf2.size()) < maxLengthDifference;
    }

    /**
     * @param psf1
     * @param psf2
     * @param edgeToTransform
     * @return
     */
    private Collection<StringRewriteRule> getTransformRules(PhoneSubForm psf1,
                                                            PhoneSubForm psf2, Side edgeToTransform) {
        List<StringRewriteRule> result = new ArrayList<StringRewriteRule>(2);
        PhoneSubForm shorter, longer;
        if (psf1.size() >= psf2.size()) {
            longer = psf1;
            shorter = psf2;
        } else {
            longer = psf2;
            shorter = psf1;
        }
        SimplePhoneStringDistance phoneStringDistance = SimplePhoneStringDistance
                .createInstance(shorter, longer);
        List<PhoneTransform> transforms = phoneStringDistance.getEditScript();
        SimpleStringRewriteRule original = SimpleStringRewriteRule
                .createSimpleEdge(transforms,
                        edgeToTransform, subformString);
        SimpleStringRewriteRule reverse = SimpleStringRewriteRule
                .createSimpleEdge(PhoneTransform.reverseAll(transforms),
                        edgeToTransform, subformString);
        result.add(original);
        result.add(reverse);
        return result;
    }

    private Side edgesAreEqual(PhoneSubForm psf1, PhoneSubForm psf2) {
        if (psf1.getEdgemost(Side.LEFT) == psf2.getEdgemost(Side.LEFT)) {
            if (psf1.getEdgemost(Side.RIGHT) == psf2.getEdgemost(Side.RIGHT)) {
                return Side.EITHER;
            } else {
                return Side.LEFT;
            }
        } else if (psf1.getEdgemost(Side.RIGHT) == psf2.getEdgemost(Side.RIGHT)) {
            return Side.RIGHT;

        }
        return Side.NEITHER;
    }

}
