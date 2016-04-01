/**
 *
 */
package learn.batch;

import eval.Evaluation;
import forms.FormPair;
import grammar.Grammar;
import learn.ViolatedCandidate;
import learn.update.UpdateAction;
import learn.update.UpdateAlgorithm;
import util.debug.Timer;

import java.util.concurrent.Callable;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public class LearningStep implements Callable<Boolean> {
    private Grammar grammar;
    private final FormPair learningDatum;
    private final double plasticity;
    private final LearningProperties properties;

    /**
     * @param properties
     * @param grammar
     * @param learningDatum
     * @param plasticity
     */
    private LearningStep(LearningProperties properties, Grammar grammar, FormPair learningDatum, double plasticity) {
        this.grammar = grammar;
        this.learningDatum = learningDatum;
        this.plasticity = plasticity;
        this.properties = properties;
    }

    public static LearningStep getInstance(LearningProperties learningProperties, Grammar grammar, FormPair learningDatum, double plasticity) {
        return new LearningStep(learningProperties, grammar, learningDatum, plasticity);
    }

    public Boolean call() throws Exception {
        Timer timer = new Timer();
        FormPair testPair = learningDatum.getUnlabeled(properties.getDirection());
        Evaluation freeEvaluation = grammar.evaluate(testPair, true,
                properties.getEvaluationNoise());
        ViolatedCandidate lCandidate = freeEvaluation.getWinner();
        // timer.reportElapsedTime(String.format("Time to get first winner - %s",lCandidate.getCandidate()), true);

        if (correspondsWithLearningDatum(lCandidate)) {
            return true;
        }

        Evaluation parsedEvaluation = grammar.evaluate(learningDatum, properties.isResample(),
                properties.getEvaluationNoise());
        ViolatedCandidate tCandidate = parsedEvaluation.getWinner();
        // timer.reportElapsedTime(String.format("Time to get second winner - %s",tCandidate.getCandidate()), true);
        UpdateAlgorithm updateAlgorithm = properties.getUpdateAlgorithm();
        UpdateAction action = updateAlgorithm.getUpdate(grammar.getRankedCon(), lCandidate, tCandidate, plasticity);
        action.updateRanking(grammar.getRankedCon());
        //System.out.println(action.toPrettyString());

        return false;
    }

    /**
     * @param lCandidate
     */
    private boolean correspondsWithLearningDatum(ViolatedCandidate lCandidate) {
        return lCandidate.getCandidate().containsForm(learningDatum.right());
    }


}
