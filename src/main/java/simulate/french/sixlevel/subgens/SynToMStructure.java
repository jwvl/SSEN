/**
 *
 */
package simulate.french.sixlevel.subgens;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import forms.morphosyntax.MStructure;
import forms.morphosyntax.MStructureFactory;
import forms.morphosyntax.SemSynForm;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import gen.mapping.SubCandidateSet;
import gen.subgen.SubGen;
import grammar.levels.Level;
import simulate.french.sixlevel.constraints.factories.AgreeConstraintFactory;
import simulate.french.sixlevel.constraints.factories.ForbidExpressConstraintFactory;

import java.util.Collection;
import java.util.List;

/**
 * Custom class to build MStructure from SemSynForms.
 *
 * @author jwvl
 * @date Jan 19, 2015
 */
public class SynToMStructure extends SubGen<SemSynForm, MStructure> {

    public SynToMStructure(Level leftLevel, Level rightLevel) {
        super(leftLevel, rightLevel);
        addConstraintFactory(AgreeConstraintFactory.createInstance());
        //addConstraintFactory(DemandExpressConstraintFactory.createInstance());
        addConstraintFactory(ForbidExpressConstraintFactory.createInstance());
    }

    /*
     * (non-Javadoc)
     *
     * @see gen.SubGen#getRightFunction()
     */
    @Override
    protected Function<SemSynForm, SubCandidateSet> getRightFunction() {
        return new Function<SemSynForm, SubCandidateSet>() {

            @Override
            public SubCandidateSet apply(SemSynForm input) {
                // Check if input is of correct level
                Collection<FormMapping> mappings = Lists.newArrayList();
                MStructureFactory factory = MStructureFactory
                        .createInstance(input);
                List<MStructure> mStructures = factory.generateAllStructures();
                for (MStructure ms : mStructures) {
                    mappings.add(PairMapping.createInstance(input, ms));
                }
                return SubCandidateSet.of(mappings);
            }
        };
    }

    /* (non-Javadoc)
     * @see gen.subgen.SubGen#cachesPairs()
     */
    @Override
    protected boolean cachesPairs() {
        return false;
    }


}
