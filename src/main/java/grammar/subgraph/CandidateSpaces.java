package grammar.subgraph;

import com.google.common.collect.ListMultimap;
import forms.Form;
import forms.FormPair;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import grammar.dynamic.DynamicNetworkGrammar;
import graph.explorer.CorrectCandidateFinder;
import learn.PairDistribution;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by janwillem on 30/03/16.
 */
public class CandidateSpaces {
    private final Map<FormPair, CandidateGraph> spaces;

    public CandidateSpaces(Map<FormPair, CandidateGraph> spaces) {
        this.spaces = spaces;
    }

    public CandidateSpaces() {
        this(new HashMap<>());
    }

    public void add(CandidateGraph space) {
        spaces.put(space.getFormPair(), space);
    }

    public List<FormMapping> filter(FormPair formPair, Collection<FormMapping> toFilter) {
        return spaces.get(formPair).filter(toFilter);
    }

    public SubCandidateSet filterSet(FormPair formPair, SubCandidateSet toFilter) {
        CandidateGraph candidateGraph = spaces.get(formPair);
        List<FormMapping> result = candidateGraph.filter(toFilter.getContents());
        return SubCandidateSet.of(result);
    }

    public static CandidateSpaces fromDistribution(PairDistribution pairDistribution, DynamicNetworkGrammar grammar) {

        Collection<FormPair> allPairs = pairDistribution.getKeys();
        Map<FormPair, CandidateGraph> map = new HashMap<>();
        for (FormPair fp : allPairs) {
            ListMultimap<Form, Form> greenMap = CorrectCandidateFinder.generateCandidateSpace(grammar, fp);
            CandidateGraph candidateGraph = new CandidateGraph(fp, greenMap);
            map.put(fp, candidateGraph);
        }
        return new CandidateSpaces(map);
    }
}
