package grammar.tools;

import eval.Evaluation;
import forms.FormPair;
import grammar.Grammar;
import graph.Direction;
import learn.ViolatedCandidate;
import learn.data.LearningData;
import learn.stats.ErrorCounter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by janwillem on 30/03/16.
 */
public class GrammarTester {

    public static void testGrammarOnLearningData(Grammar grammar, LearningData learningData, int numTests, double evaluationNoise) {
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
    }
}
