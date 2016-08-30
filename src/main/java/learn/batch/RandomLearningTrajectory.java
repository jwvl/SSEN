package learn.batch;

import eval.Evaluation;
import eval.sample.AbstractSampler;
import eval.sample.UniformRandomSampler;
import forms.Form;
import forms.FormPair;
import grammar.Grammar;
import learn.PairDistribution;
import learn.ViolatedCandidate;
import learn.data.LearningData;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

/**
 * Created by janwillem on 29/08/16.
 */
public class RandomLearningTrajectory extends AbstractLearningTrajectory {
    private final int numEvaluations;
    private final AbstractSampler randomSampler;
    private int counter;
    private boolean winningHierarchyFound;

    public RandomLearningTrajectory(Grammar grammar, LearningData data, LearningProperties learningProperties, int numEvaluations) {
        super(grammar, data, learningProperties);
        this.numEvaluations = numEvaluations;
        this.randomSampler = UniformRandomSampler.createInstance(1.0);
        this.counter = 0;
        this.winningHierarchyFound = true;
    }

    @Override
    public void run(ExecutorService executorService) {
        boolean errorEncountered = true;
        PairDistribution learningPairs = (PairDistribution) getData();
        Collection<FormPair> allPairs = learningPairs.getKeys();

        while (counter < numEvaluations && errorEncountered == true) {
            errorEncountered = false;
            for (FormPair formPair: allPairs) {
                Evaluation eval = getGrammar().evaluate(formPair,true,1.0);
                if (!winnerMatches(formPair,eval)) {
                    errorEncountered = true;
                    break;
                }

            }
            counter++;
        }
        this.winningHierarchyFound = !errorEncountered;
    }

    private static boolean winnerMatches(FormPair originalPair, Evaluation eval) {
        Form right = originalPair.right();
        ViolatedCandidate violatedCandidate = eval.getWinner();
        return violatedCandidate.getCandidate().containsForm(right);
    }

    public boolean isWinningHierarchyFound() {
        return winningHierarchyFound;
    }
}
