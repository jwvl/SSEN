/**
 *
 */
package simulate.french.sixlevel.constraints.factories;

import com.google.common.collect.Lists;
import com.typesafe.config.ConfigFactory;
import forms.morphosyntax.Morpheme;
import forms.morphosyntax.SyntacticCategory;
import forms.phon.LexicalMapping;
import gen.mapping.FormMapping;
import gen.mapping.specific.MfUfMapping;
import simulate.french.sixlevel.constraints.LexicalConstraint;
import simulate.french.sixlevel.helpers.LexicalHypothesisRepository;

import java.util.Collection;
import java.util.Collections;

/**
 * @author jwvl
 * @date Jul 28, 2015
 */
public class LexicalConstraintFactory extends
        SubmappingConstraintFactory<MfUfMapping, LexicalMapping> {
    public static double SHORTEST_FORM_BIAS = ConfigFactory.load().getDouble("constraints.shortestFormBias");
    private LexicalHypothesisRepository lexicalHypotheses;

    public LexicalConstraintFactory(
            LexicalHypothesisRepository lexicalHypotheses) {
        this.lexicalHypotheses = lexicalHypotheses;
        for (Morpheme morpheme : lexicalHypotheses) {
            for (LexicalMapping lexicalMapping : lexicalHypotheses
                    .getSubmappings(morpheme)) {
                getConstraint(lexicalMapping);
            }
        }
        printConstraintMap();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.OffendingPairConstraintFactory
     * #createConstraint(java.lang.Object, java.lang.Object)
     */
    @Override
    public LexicalConstraint createConstraint(LexicalMapping lexicalMapping) {
        if (isMinimalMapping(lexicalMapping) && lexicalMapping.left().getSyntacticCategory() == SyntacticCategory.N) {
            return LexicalConstraint.createInstance(lexicalMapping, SHORTEST_FORM_BIAS);
        }

        return LexicalConstraint.createInstance(lexicalMapping);
    }

    /**
     * @param lexicalMapping
     * @return
     */
    private boolean isMinimalMapping(LexicalMapping lexicalMapping) {

        return lexicalHypotheses.getMinimalMappings().contains(lexicalMapping);

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * simulate.french.sixlevel.constraints.factories.OffendingPairConstraintFactory
     * #getOffenders(graph.Transgressor)
     */
    @Override
    public Collection<LexicalMapping> getOffenders(MfUfMapping transgressor) {
        Collection<LexicalMapping> result = Lists.newArrayList();
        Collections.addAll(result, transgressor.getLexicalMappings());
        return result;
    }

    /* (non-Javadoc)
     * @see constraints.factories.ConstraintFactory#addFormMapping(gen.mapping.FormMapping)
     */
    @Override
    public void addFormMapping(FormMapping fm) {
        if (fm instanceof MfUfMapping) {
            addTransgressor((MfUfMapping) fm);
        }

    }


}
