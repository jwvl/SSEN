package io.candidates;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import forms.Form;
import grammar.subgraph.CandidateGraph;

import java.util.List;
import java.util.Map;

/**
 * Created by janwillem on 04/03/2017.
 */
public class CandidateGraphWriter {
    private final CandidateGraph candidateGraph;
    private final Map<Form,Integer> formIndices;
    private int formCounter;

    public CandidateGraphWriter(CandidateGraph candidateGraph) {
        this.candidateGraph = candidateGraph;
        this.formIndices = Maps.newHashMap();
        this.formCounter = 0;
        makeIndices();
    }

    private void makeIndices() {
        Map<Form,List<Form>> forms = candidateGraph.getMap();
        for (Form in: forms.keySet()) {
            getFormIndex(in);
            for (Form out: forms.get(in)) {
                getFormIndex(out);
            }
        }
    }

    public List<String> writeNodes(String prefix) {
        List<String> result = Lists.newArrayList();
        for (Form form: formIndices.keySet()) {
            String toAdd = prefix + "\t" +
                    form + "\t" + getFormIndex(form) +"\t" + form.getLevelIndex();
            result.add(toAdd);
        }
        return result;
    }

    public List<String> writeEdges(String prefix) {
        List<String> result = Lists.newArrayList();
        Map<Form,List<Form>> edges = candidateGraph.getMap();
        for (Form in: edges.keySet()) {
            int inIndex = getFormIndex(in);
            for (Form out: edges.get(in)) {
                int outIndex = getFormIndex(out);
                String toAdd = prefix +"\t"+inIndex+"\t"+outIndex;
                result.add(toAdd);
            }
        }
        return result;
    }

    private int getFormIndex(Form form) {
        if (formIndices.containsKey(form)) {
            return formIndices.get(form);
        } else {
            int result = formCounter++;
            formIndices.put(form, result);
            return result;
        }
    }
}
