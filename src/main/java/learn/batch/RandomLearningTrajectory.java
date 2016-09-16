package learn.batch;

import constraints.hierarchy.reimpl.Hierarchy;
import eval.Evaluation;
import eval.sample.AbstractSampler;
import eval.sample.UniformRandomSampler;
import forms.Form;
import forms.FormPair;
import grammar.Grammar;
import grammar.dynamic.DynamicNetworkGrammar;
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
    private Hierarchy lastHierarchy;

    public RandomLearningTrajectory(Grammar grammar, LearningData data, LearningProperties learningProperties, int numEvaluations) {
        super(grammar, data, learningProperties);
        this.numEvaluations = numEvaluations;
        this.randomSampler = UniformRandomSampler.createInstance(1.0);
        this.counter = 0;
        this.winningHierarchyFound = false;
    }

    @Override
    public void run(ExecutorService executorService) {
        boolean errorEncountered = true;
        PairDistribution learningPairs = (PairDistribution) getData();
        Collection<FormPair> allPairs = learningPairs.getKeys();
        Grammar grammar = getGrammar();

        while (counter < numEvaluations && errorEncountered == true) {
            errorEncountered = false;
            boolean first = true;
            for (FormPair formPair: allPairs) {
                Evaluation eval = getGrammar().evaluate(formPair,first,1.0);
                first = false;
                if (!winnerMatches(formPair,eval)) {
                    errorEncountered = true;
                    break;
                }

            }
            counter++;
        }
        this.winningHierarchyFound = !errorEncountered;
        DynamicNetworkGrammar dynamicNetworkGrammar = (DynamicNetworkGrammar) getGrammar();
        this.lastHierarchy = dynamicNetworkGrammar.getLastSampledHierarchy();
        if (winningHierarchyFound) {
            System.out.println("This hierarchy gets all forms correct!");
        }
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
