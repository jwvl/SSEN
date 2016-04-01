/**
 *
 */
package simulate.french.sixlevel.helpers;

import com.google.common.collect.Sets;
import forms.morphosyntax.Morpheme;
import forms.phon.flat.UnderlyingForm;
import forms.primitives.segment.PhoneSubForm;
import gen.rule.induction.StringPhonRuleInducer;
import gen.rule.string.StringRewriteRule;

import java.util.Collection;

/**
 * @author jwvl
 * @date Jul 25, 2015
 */
public class UfSfRuleInducer {

    private final LexicalHypothesisRepository hypotheses;
    private final StringPhonRuleInducer<UnderlyingForm> inducer;


    /**
     * @param hypotheses
     */
    public UfSfRuleInducer(LexicalHypothesisRepository hypotheses) {
        this.hypotheses = hypotheses;
        this.inducer = new StringPhonRuleInducer<UnderlyingForm>();
    }


    public Collection<StringRewriteRule> induceRules() {
        Collection<StringRewriteRule> result = Sets.newHashSet();
        for (Morpheme morpheme : hypotheses) {
            Collection<PhoneSubForm> allomorphs = hypotheses.getCandidates(morpheme);
            Collection<StringRewriteRule> currentRules = inducer.induce(allomorphs);
            result.addAll(currentRules);
        }
        return result;
    }

}
