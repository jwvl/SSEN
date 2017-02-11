package learn.batch;

import constraints.hierarchy.reimpl.Hierarchy;
import eval.Evaluation;
import eval.sample.AbstractSampler;
import eval.sample.UniformRandomSampler;
import forms.Form;
import forms.FormPair;
import grammar.Grammar;
import grammar.dynamic.DynamicNetworkGrammar;
import graph.Direction;
import learn.data.PairDistribution;
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
    private static final int ERROR_EVERY = 1000;

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
        int recordCorrect = 0;

        while (counter < numEvaluations && errorEncountered == true) {
            errorEncountered = false;
            int numCorrect = 0;
            boolean first = true;
            for (FormPair formPair: allPairs) {
                Evaluation eval = getGrammar().evaluate(formPair.getUnlabeled(Direction.RIGHT),first,1.0);
                first = false;
                if (!winnerMatches(formPair,eval)) {
                    errorEncountered = true;
                    break;
                } else {
                    numCorrect++;
                    if (numCorrect > recordCorrect) {
                        recordCorrect = numCorrect;

                        System.out.println("At try " + counter +", record correct is \t" + recordCorrect);
                    }
                }

            }
            counter++;
            if (counter % ERROR_EVERY == 0) {
                //System.out.println(counter+"\t"+recordCorrect);
            }
        }
        this.winningHierarchyFound = !errorEncountered;
        DynamicNetworkGrammar dynamicNetworkGrammar = (DynamicNetworkGrammar) getGrammar();
        this.lastHierarchy = dynamicNetworkGrammar.getLastSampledHierarchy();
        if (winningHierarchyFound) {
            System.out.println("The hierarchy at try " +counter + " gets all forms correct!");
        } else {
            System.out.println(counter +"\t"+recordCorrect);
        }
    }

    private static boolean winnerMatches(FormPair originalPair, Evaluation eval) {
        Form right = originalPair.right();
        ViolatedCandidate violatedCandidate = eval.getWinner();
        boolean result = violatedCandidate.getCandidate().containsForm(right);
        return result;
    }

    public boolean isWinningHierarchyFound() {
        return winningHierarchyFound;
    }
}
