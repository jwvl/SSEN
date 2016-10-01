package simulate.analysis.statistics;

import candidates.Candidate;
import com.google.common.collect.Maps;
import eval.Evaluation;
import forms.FormPair;
import grammar.Grammar;
import graph.Direction;
import learn.ViolatedCandidate;
import util.collections.FrequencyMap;

import java.util.List;
import java.util.Map;

/**
 * Created by janwillem on 29/09/16.
 */
public class CandidateSetCollector {
    private final List<FormPair> inputsToTest;
    private final FrequencyMap<FormpairCandidateMap> frequencies;
    private final CandidateNamer candidateNamer;
    private final Direction direction;

    public CandidateSetCollector(List<FormPair> inputsToTest, Direction direction) {
        this.inputsToTest = inputsToTest;
        this.direction = direction;
        this.frequencies = new FrequencyMap<>();
        this.candidateNamer = new CandidateNamer(inputsToTest);
    }

    public void addTests(Grammar grammar, int numPerInput, double evaluationNoise) {
        for (int i=0; i < numPerInput; i++) {
            FormpairCandidateMap set = test(grammar, evaluationNoise);
            frequencies.addOne(set);
        }
    }

    private FormpairCandidateMap test(Grammar grammar, double evaluationNoise) {
        Map<FormPair,Candidate> result = Maps.newHashMap();
        for (FormPair formPair: inputsToTest) {
            Evaluation evaluation = grammar.evaluate(formPair.getUnlabeled(direction), true, evaluationNoise);
            ViolatedCandidate candidate = evaluation.getWinner();
            result.put(formPair,candidate.getCandidate());
        }
        return new FormpairCandidateMap(result);
    }

    public String getResults() {
        StringBuilder stringBuilder = new StringBuilder();
        for (FormpairCandidateMap candidateSet: frequencies.getKeys()) {
            stringBuilder.append(candidateNamer.getSetName(candidateSet));
            stringBuilder.append("\t").append(frequencies.getCount(candidateSet));
            stringBuilder.append("\n");
        }
        for (String candidateWithAbbreviation: candidateNamer.getAllCandidates()) {
            stringBuilder.append("\n").append(candidateWithAbbreviation);
        }
        return stringBuilder.toString();


    }
}
