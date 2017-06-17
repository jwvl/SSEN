/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.Sets;
import forms.morphosyntax.*;
import forms.primitives.feature.AbstractMFeature2;
import simulate.french.sixlevel.constraints.ForbidExpressConstraint;

import java.util.Collection;

/**
 * @author jwvl
 * @date Aug 1, 2015
 */
public class ForbidExpressConstraintFactory extends
        SubformConstraintFactory<MStructure, AffixType> {

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.SubformConstraintFactory
     * #createConstraint(java.lang.Object)
     */
    @Override
    public ForbidExpressConstraint createConstraint(AffixType offender) {
        return ForbidExpressConstraint.createFromAffixType(offender);
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
        for (SyntacticWord syntacticWord : transgressor) {
            SyntacticCategory category = syntacticWord.getSyntacticCategory();
            for (MElement element : syntacticWord) {
                // Only add constraints for non-concept features that do not express value
                if (element.getType() != MFeatureType.FIXED && !element.isConcept()) {
                    AbstractMFeature2 feature = element.getFeature();
                    if (!feature.isNull()) {
                        AffixType affixType = AffixType.createInstance(category,
                                element.getFeature().attribute);
                        result.add(affixType);
                    }
                }
            }
        }
        return result;
    }

    /**
     * @return
     */
    public static ForbidExpressConstraintFactory createInstance() {
        return new ForbidExpressConstraintFactory();
    }

}
