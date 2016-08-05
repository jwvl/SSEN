/**
 *
 */
package learn;

import candidates.AbstractInput;
import candidates.Candidate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import forms.Form;
import forms.FormChain;
import forms.GraphForm;
import constraints.Constraint;
import constraints.helper.ConstraintArrayList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author jwvl
 * @date Aug 9, 2015
 */
public class ViolatedCandidateBuilder {
    private Multiset<Constraint> violated;
    private List<Form> formsInCandidate;

    public ViolatedCandidateBuilder() {
        violated = HashMultiset.create();
        formsInCandidate = Lists.newArrayList();
    }

    public ViolatedCandidate build(AbstractInput input, boolean reverseForms) {
        Multiset<Constraint> multiset = ImmutableMultiset.copyOf(violated);
        if (reverseForms) {
            Collections.reverse(formsInCandidate);
        }
        FormChain asChain = FormChain.fromFormstoSimpleMappings(formsInCandidate);
        Candidate candidate = Candidate.fromInputAndChain(input, asChain);
        ViolatedCandidate result = new ViolatedCandidate(multiset, candidate);
        return result;
    }

    public void addForm(Form f) {
        if (!(f instanceof GraphForm)) {
            formsInCandidate.add(f);
        }
    }

    public void addConstraint(Constraint constraint, int numToAdd) {
        for (int i = 0; i < numToAdd; i++) {
            violated.add(constraint);
        }
    }

    public void addConstraints(Constraint... constraints) {
        Collections.addAll(violated, constraints);
    }

    public void addConstraintsFromList(ConstraintArrayList constraints) {
        for (Constraint c : constraints) {
            violated.add(c);
        }
    }

    /**
     * @param constraints
     */
    public void addConstraints(Collection<Constraint> constraints) {
        violated.addAll(constraints);
    }
}
