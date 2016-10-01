package grammar.tools;

import candidates.Candidate;
import com.google.common.collect.Sets;
import eval.Evaluation;
import forms.Form;
import forms.FormPair;
import grammar.Grammar;
import graph.Direction;
import learn.data.PairDistribution;
import learn.ViolatedCandidate;
import learn.data.LearningData;
import learn.stats.ErrorCounter;
import simulate.analysis.CandidateMappingTable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by janwillem on 30/03/16.
 */
public class GrammarTester {

    public static double testGrammarOnLearningData(Grammar grammar, LearningData learningData, int numTests, double evaluationNoise) {
        ErrorCounter totalCounter = new ErrorCounter();
        Map<FormPair, ErrorCounter> errorsPerPair = new HashMap<>();
        int count = 0;
        while (count < numTests && learningData.hasNext()) {
            FormPair test = learningData.next();
            Evaluation evaluation = grammar.evaluate(test.getUnlabeled(Direction.RIGHT), true, evaluationNoise);
            ViolatedCandidate candidate = evaluation.getWinner();
            boolean success = candidate.getCandidate().containsForm(test.right());
            totalCounter.increaseCount(!success);
            if (errorsPerPair.get(test) == null) {
                errorsPerPair.put(test, new ErrorCounter());
            }
            ErrorCounter pairErrorCounter = errorsPerPair.get(test);
            pairErrorCounter.increaseCount(!success);
            count++;
        }
        System.out.printf("Total error after %d evaluations:%n", numTests);
        for (FormPair formPair : errorsPerPair.keySet()) {
            System.out.printf("%s :: %s pct%n", formPair, errorsPerPair.get(formPair).getErrorAsPercentage());
        }
        return totalCounter.getErrorRate();
    }

    public static CandidateMappingTable getCandidateMappingTable(Grammar grammar, LearningData learningData, int numTests, double evaluationNoise) {
        CandidateMappingTable result = CandidateMappingTable.createNew();
        int count = 0;
        while (count < numTests && learningData.hasNext()) {
            FormPair test = learningData.next();
            Evaluation evaluation = grammar.evaluate(test.getUnlabeled(Direction.RIGHT), true, evaluationNoise);
            ViolatedCandidate violatedCandidate = evaluation.getWinner();
            Candidate candidate = violatedCandidate.getCandidate();
            result.addCandidate(candidate,1);
            count++;
        }
        return result;
    }

    public static CandidateMappingTable getCandidateMappingTable(Grammar grammar, FormPair formPair, int numTests, double evaluationNoise) {
        CandidateMappingTable result = CandidateMappingTable.createNew();
        int count = 0;
        while (count < numTests) {
            FormPair test = formPair;
            Evaluation evaluation = grammar.evaluate(test.getUnlabeled(Direction.RIGHT), true, evaluationNoise);
            ViolatedCandidate violatedCandidate = evaluation.getWinner();
            Candidate candidate = violatedCandidate.getCandidate();
            result.addCandidate(candidate,1);
            count++;
        }
        return result;
    }

    public static PairDistribution toPairDistribution(Grammar grammar, LearningData learningData, int testsPerInput, double evaluationNoise) {
        Set<FormPair> unlabeledPairs = Sets.newHashSet();
        PairDistribution result = new PairDistribution(UUID.randomUUID().toString());
        for (FormPair labeled: learningData.getKeys()) {
            unlabeledPairs.add(labeled.getUnlabeled(Direction.RIGHT));
        }
        for (FormPair unlabeled: unlabeledPairs) {
            for (int i = 0; i < testsPerInput; i++) {
                Candidate winncandidate = evaluateToCandidate(grammar,unlabeled,true,evaluationNoise);
                Form[] winningForms = winncandidate.getForms();
                Form rightmost = winningForms[winningForms.length-1];
                result.addOne(unlabeled.left(),rightmost);
            }
        }
        return result;

    }

    private static Candidate evaluateToCandidate(Grammar grammar, FormPair formPair, boolean resample, double evaluationNoise) {
        Evaluation evaluation = grammar.evaluate(formPair, resample, evaluationNoise);
        ViolatedCandidate violatedCandidate = evaluation.getWinner();
        return violatedCandidate.getCandidate();
    }
}
