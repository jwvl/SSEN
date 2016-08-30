package simulate.analysis;

import candidates.AbstractInput;
import forms.Form;
import forms.FormChain;
import grammar.levels.Level;
import util.collections.FrequencyTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by janwillem on 20/08/16.
 */
public class CandidateAnalyzer {
    private final Map<AbstractInput, FrequencyTable<Level, Form>> formsPerInput;

    private CandidateAnalyzer(Map<AbstractInput, FrequencyTable<Level, Form>> formsPerInput) {
        this.formsPerInput = formsPerInput;
    }

    public static CandidateAnalyzer createNew() {
        return new CandidateAnalyzer(new HashMap<>());
    }

    private void addForms(AbstractInput input, Form form, int count) {
        FrequencyTable<Level,Form> frequencyTableForLevel = formsPerInput.get(input);
        Level level = form.getLevel();
        if (input == null) {
            frequencyTableForLevel = new FrequencyTable<>();
            formsPerInput.put(input,frequencyTableForLevel);
        }
        frequencyTableForLevel.add(level,form,count);
    }

    public void addFormChains(AbstractInput input, FormChain formChain, int count) {
        for (Form form: formChain.getContents()) {
            addForms(input,form,count);
        }
    }
}
