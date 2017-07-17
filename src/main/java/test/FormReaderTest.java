package test;

import candidates.AbstractInput;
import candidates.Candidate;
import candidates.FormInput;
import com.google.common.collect.Lists;
import constraints.Constraint;
import constraints.RankedConstraint;
import constraints.helper.ConstraintArrayList;
import constraints.hierarchy.reimpl.Hierarchy;
import forms.morphosyntax.MForm;
import forms.morphosyntax.MFormFactory;
import forms.morphosyntax.MStructure;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import grammar.levels.predefined.BiPhonSix;
import io.tableau.SimpleTableau;
import io.tableau.SimpleTableauBuilder;
import learn.ViolatedCandidate;
import simulate.french.sixlevel.subgens.MStructureToMForm;

import java.util.List;

/**
 * Created by janwillem on 15/07/2017.
 */
public class FormReaderTest {
    public static void main(String[] args) {
        String mstructureString = "ADJ{bon g=M num=SG}, N{acteur.g=M num=SG}";
        String mstructureString2 = "N{acteur.g=M num=SG}";
        MStructure mStructure = MStructure.readFromString(mstructureString);
        MStructure mStructure2 = MStructure.readFromString(mstructureString2);
        System.out.println(mStructure);
        MStructureToMForm mStructureToMForm = new MStructureToMForm(BiPhonSix.getMstructureLevel(), BiPhonSix.getMformLevel());
        MFormFactory formFactory = MFormFactory.createInstance();
        List<MForm> mFormList = formFactory.generateAllForms(mStructure);
        for (MForm mForm: mFormList) {
            System.out.println(mForm.toBracketedString());
        }
        List<MForm> mFormList2 = formFactory.generateAllForms(mStructure2);
        for (MForm mForm: mFormList2) {
            System.out.println(mForm.toBracketedString());
        }
        SubCandidateSet candidates = mStructureToMForm.generateRight(mStructure);
        for (FormMapping formMapping: candidates) {
            mStructureToMForm.getAssociatedConstraints(formMapping);
        }
        List<Constraint> allConstraints = Constraint.getAllCreated();
        List<RankedConstraint> withRanking = Lists.newArrayListWithCapacity(allConstraints.size());
        for (int i=0; i < allConstraints.size(); i++) {
            double ranking = 100.0 + ((double) i);
            RankedConstraint rankedConstraint = RankedConstraint.of(allConstraints.get(i),ranking);
            withRanking.add(rankedConstraint);
        }
        Hierarchy hierarchy = Hierarchy.createNew();
        for (RankedConstraint rc: withRanking) {
            hierarchy.addConstraint(rc.getConstraint(), rc.getRanking());
            hierarchy.putValue(rc.getConstraint(),rc.getRanking());
        }
        Hierarchy sampled = hierarchy.sampleTinyNoise();
        AbstractInput input = FormInput.createInstance(mStructure);
        SimpleTableauBuilder simpleTableauBuilder = new SimpleTableauBuilder(sampled);
        for (FormMapping formMapping: candidates) {
            ConstraintArrayList constraints = mStructureToMForm.getAssociatedConstraints(formMapping);
            Candidate candidate = Candidate.fromInputAndForms(input,formMapping.left(),formMapping.right());
            ViolatedCandidate vc = new ViolatedCandidate(constraints,candidate);
            simpleTableauBuilder.addViolatedCandidate(vc);
        }
        SimpleTableau tableau = simpleTableauBuilder.build();
        System.out.println(tableau.toSeparatedString(",", true));

    }
}
