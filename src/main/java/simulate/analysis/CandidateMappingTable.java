package simulate.analysis;

import candidates.Candidate;
import forms.Form;
import util.collections.FrequencyTable;

import java.util.Set;

/**
 * Created by janwillem on 20/08/16.
 */
public class CandidateMappingTable {
    private final FrequencyTable<Form, Form> mappingFrequencies;

    private CandidateMappingTable(FrequencyTable<Form, Form> mappingFrequencies) {
        this.mappingFrequencies = mappingFrequencies;
    }

    public static CandidateMappingTable createNew() {
        return new CandidateMappingTable(new FrequencyTable<>());
    }

    public void addCandidate(Candidate candidate, int count) {
        Form[] formChain = candidate.getForms();
        for (int i=0; i < formChain.length-1;i++) {
            mappingFrequencies.add(formChain[i],formChain[i+1],count);
        }
    }


    public FrequencyTable<Form,Form> getFrequencyTable() {
        return mappingFrequencies;
    }

    public String toJsRows() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[\n");
        Set<Form> fromForms = mappingFrequencies.getColumnSet();
        for (Form from: fromForms) {
            Set<Form> toForms = mappingFrequencies.getRowsForColumn(from);
            for (Form to: toForms) {
                int frequency = mappingFrequencies.getCount(from,to);
                stringBuilder.append("['").append(from).append("', ");
                stringBuilder.append("'").append(to).append("', ");
                stringBuilder.append(frequency).append(" ],\n");
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public CandidateMappingTable mergeWith(CandidateMappingTable toMerge) {
        FrequencyTable<Form,Form> mappings = new FrequencyTable<>();
        mappings.addAll(this.getFrequencyTable());
        mappings.addAll(toMerge.getFrequencyTable());
        return new CandidateMappingTable(mappings);

    }
}
