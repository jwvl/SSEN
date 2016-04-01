package learn.stats;

import candidates.Candidate;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import forms.Form;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by janwillem on 31/03/16.
 */
public class CandidateRepository {
    private Map<Form, Multiset<Candidate>> formsPerInput;

    private CandidateRepository(Map<Form, Multiset<Candidate>> formsPerInput) {
        this.formsPerInput = formsPerInput;
    }

    public static CandidateRepository createNew() {
        Map<Form, Multiset<Candidate>> emptyMap = new HashMap<Form, Multiset<Candidate>>();
        return new CandidateRepository(emptyMap);
    }

    public void addCandidate(Candidate toAdd) {
        Form inputForm = toAdd.getForms()[0];
        Multiset<Candidate> toIncrease = formsPerInput.getOrDefault(inputForm, HashMultiset.create());
        toIncrease.add(toAdd);
    }

}
