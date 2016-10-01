package simulate.analysis.statistics;

import candidates.Candidate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import forms.FormPair;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by janwillem on 29/09/16.
 */
public class CandidateNamer {
    private final List<FormPair> formPairs;
    private final Map<FormPair,Map<Candidate,Integer>> mappings;
    private final int[] counts;

    public CandidateNamer(List<FormPair> formPairs) {
        this.formPairs = formPairs;
        this.mappings = Maps.newHashMap();
        for (FormPair formPair: formPairs) {
            Map<Candidate,Integer> newMap = Maps.newHashMap();
            mappings.put(formPair,newMap);
        }
        this.counts = new int[formPairs.size()];
    }

    public String getCandidateName(FormPair formpair, Candidate candidate) {
        int pairIndex = formPairs.indexOf(formpair);
        if (pairIndex < 0) {
            return "ERROR";
        }
        Map<Candidate,Integer> map = mappings.get(formpair);
        if (!map.containsKey(candidate)) {
            counts[pairIndex]++;
            map.put(candidate, counts[pairIndex]);
        }
        int candidateNumber = map.get(candidate);
        char pairLetter = (char) (65 + pairIndex);
        return pairLetter+String.valueOf(candidateNumber);
    }

    public String getSetName(FormpairCandidateMap candidateSet) {
        Map<FormPair,Candidate> map = candidateSet.getMap();
        String[] asArray = new String[map.size()];
        int count = 0;
        for (FormPair formPair: map.keySet()) {
            Candidate candidate = map.get(formPair);
            asArray[count++] = getCandidateName(formPair,candidate);
        }
        Arrays.sort(asArray);
        return Arrays.toString(asArray);
    }

    public List<String> getAllCandidates() {
        List<String> result = Lists.newArrayList();
        for (FormPair formPair: formPairs) {
            for (Candidate candidate: mappings.get(formPair).keySet()) {
                String abbreviation = getCandidateName(formPair,candidate);
                String candidateName = candidate.outputToBracketedString();
                result.add(abbreviation+" :"+candidateName);
            }
        }
        return result;
    }
}
