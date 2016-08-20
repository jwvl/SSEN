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

import java.util.concurrent.Callable;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public class LearningStep implements Callable<UpdateAction> {
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

    public UpdateAction call() throws Exception {
        FormPair testPair = learningDatum.getUnlabeled(properties.getDirection());
        Evaluation freeEvaluation = grammar.evaluate(testPair, true,
                properties.getEvaluationNoise());
        ViolatedCandidate lCandidate = freeEvaluation.getWinner();

        if (correspondsWithLearningDatum(lCandidate)) {
            return UpdateAction.NO_UPDATE;
        }

        Evaluation parsedEvaluation = grammar.evaluate(learningDatum, properties.isResample(),
                properties.getEvaluationNoise());
        ViolatedCandidate tCandidate = parsedEvaluation.getWinner();
        UpdateAlgorithm updateAlgorithm = properties.getUpdateAlgorithm();
        UpdateAction action = updateAlgorithm.getUpdate(grammar.getHierarchy(), lCandidate, tCandidate, plasticity);
        action.updateRanking(grammar.getHierarchy());
        System.out.println("First winner: "+lCandidate.toPrettyString());
        System.out.println("Second winner: "+tCandidate.toPrettyString());
        System.out.println(action.toPrettyString());

        return action;
    }

    /**
     * @param lCandidate
     */
    private boolean correspondsWithLearningDatum(ViolatedCandidate lCandidate) {
        return lCandidate.getCandidate().containsForm(learningDatum.right());
    }


}
