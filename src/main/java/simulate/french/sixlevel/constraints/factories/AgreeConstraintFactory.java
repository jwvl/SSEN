/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.Sets;
import forms.morphosyntax.AffixType;
import forms.morphosyntax.Agreement;
import forms.morphosyntax.MStructure;
import simulate.french.sixlevel.constraints.AgreeConstraint;

import java.util.Collection;

/**
 * @author jwvl
 * @date Aug 1, 2015
 */
public class AgreeConstraintFactory extends SubformConstraintFactory<MStructure, AffixType> {

    private AgreeConstraintFactory() {
        super();
    }

    public static AgreeConstraintFactory createInstance() {
        return new AgreeConstraintFactory();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.SubformConstraintFactory
     * #createConstraint(java.lang.Object)
     */
    @Override
    public AgreeConstraint createConstraint(AffixType offender) {
        return new AgreeConstraint(offender);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.SubformConstraintFactory
     * #getOffenders(graph.Transgressor)
     */
    @Override
    public Collection<AffixType> findOffenders(MStructure transgressor) {
        Collection<AffixType> result = Sets.newHashSet();
        for (Agreement agreement : transgressor.getAgreements()) {
            if (!agreement.valuesAgree()) {
                result.add(agreement.getAffixType());
            }
        }
        return result;
    }

}
