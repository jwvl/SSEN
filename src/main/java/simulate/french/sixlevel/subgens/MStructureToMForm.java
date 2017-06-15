/**
 *
 */
package simulate.french.sixlevel.subgens;

import com.google.common.base.Function;
import com.typesafe.config.ConfigFactory;
import forms.morphosyntax.MForm;
import forms.morphosyntax.MFormFactory;
import forms.morphosyntax.MStructure;
import gen.constrain.MFormEdgeConstrainer;
import gen.mapping.FormMapping;
import gen.mapping.PairMapping;
import gen.mapping.SubCandidateSet;
import gen.subgen.SubGen;
import grammar.levels.Level;
import simulate.french.sixlevel.constraints.factories.MorphAlignConstraintFactory;
import simulate.french.sixlevel.constraints.factories.TypedAnalyzeConstraintFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom class to build MStructure from SemSynForms.
 *
 * @author jwvl
 * @date Jan 19, 2015
 */
public class MStructureToMForm extends SubGen<MStructure, MForm> {

    private final static boolean STEM_AT_EDGE_CONTAINER = ConfigFactory.load().getBoolean("gen.constrainers.stemAtEdgeConstrainer");
    private final boolean VERBOSE = false;
    private MFormFactory myFactory;

    public MStructureToMForm(Level leftLevel, Level rightLevel) {
        super(leftLevel, rightLevel);
        myFactory = MFormFactory.createInstance();
        //addConstraintFactory(new StemAlignConstraintFactory());
        //addConstraintFactory(new AnalyzeConstraintFactory());
        //addConstraintFactory(new MorphemeConstraintFactory());
        addConstraintFactory(new MorphAlignConstraintFactory());
        addConstraintFactory(new TypedAnalyzeConstraintFactory());
        if (STEM_AT_EDGE_CONTAINER) {
            addConstrainer(new MFormEdgeConstrainer());
        }
    }


    /*
     * (non-Javadoc)
     *
     * @see gen.SubGen#getRightFunction()
     */
    @Override
    protected Function<MStructure, SubCandidateSet> getRightFunction() {
        return new Function<MStructure, SubCandidateSet>() {

            @Override
            public SubCandidateSet apply(MStructure input) {


                List<MForm> mForms = myFactory.generateAllForms(input);
                List<FormMapping> result = new ArrayList<FormMapping>(mForms.size());
                if (VERBOSE) {
                    verbosePrint(input, mForms);

                }
                for (MForm mf : mForms) {
                    result.add(PairMapping.createInstance(input, mf));
                }

                return SubCandidateSet.of(result);
            }
        };
    }

    /**
     * @param input
     * @param result
     */
    protected void verbosePrint(MStructure input, List<MForm> result) {
        System.out.println("Created following forms from input " + input);
        for (MForm mf : result) {
            System.out.println(mf);
        }
    }


}
