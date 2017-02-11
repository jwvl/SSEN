/**
 *
 */
package learn.batch;

import com.typesafe.config.ConfigFactory;
import constraints.hierarchy.reimpl.Hierarchy;
import eval.Evaluation;
import forms.FormPair;
import grammar.Grammar;
import learn.ViolatedCandidate;
import learn.data.LearningData;
import learn.stats.ErrorCounter;
import learn.update.UpdateAction;
import util.debug.Timer;

import java.util.Set;
import java.util.concurrent.*;

/**
 * @author jwvl
 * @date Sep 9, 2015
 */
public class LearningTrajectory extends AbstractLearningTrajectory {

    private int numEvaluations;
    private static boolean TEST_RESULTS = false;
    private final int resetCounterEvery;
    private UpdateAction lastUpdate = UpdateAction.NO_UPDATE;



    public LearningTrajectory(Grammar grammar, LearningData data, int numEvaluations) {
        super(grammar, data);
        this.numEvaluations = numEvaluations;
        this.resetCounterEvery = calculateResetCounterEvery(numEvaluations);
    }


    public void run(ExecutorService executorService) {
        Timer totalTimer = new Timer();
        double plasticity = getLearningProperties().getInitialPlasticity();
        ErrorCounter shortCounter = new ErrorCounter();
        int numEvaluated = 0;
        ExecutorCompletionService<UpdateAction> completionService =
                new ExecutorCompletionService<UpdateAction>(executorService);
        int counter = 0;
        int epochSize = numEvaluations / getLearningProperties().getPlasticityEpochs();
        // Add learning steps to service
        while (counter < numEvaluations && getData().hasNext()) {
            FormPair randomPair = getData().next();
            LearningStep learningStep = LearningStep.getInstance(getLearningProperties(), getGrammar(), randomPair, plasticity);
            completionService.submit(learningStep);
            if (counter % epochSize == 0) {
                plasticity *= getLearningProperties().getPlasticityDecay();
            }
            counter++;
        }

        // Start evaluating
        while (numEvaluated < numEvaluations) {
            try {
                Future<UpdateAction> resultFuture = completionService.take();
                UpdateAction updateAction = resultFuture.get();
                numEvaluated++;
                boolean error = updateAction != UpdateAction.NO_UPDATE;
                shortCounter.increaseCount(error);
                if (error) {
                    lastUpdate = updateAction;
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                numEvaluated = numEvaluations;
            }

            if (shortCounter.getTotal() >= resetCounterEvery) {
                String intermediateResult = String.format("Current error rate: %s (%s pct)", shortCounter.getErrorAsRatio(), shortCounter.getErrorAsPercentage());
                addErrorRate(numEvaluated, shortCounter.getErrorRate());
                System.out.println(intermediateResult);
                //System.out.println(getGrammar().getHierarchy().printRankedConstraints());
                shortCounter.reset();
            }
        }


        if (TEST_RESULTS) {
            printTestResults();
        }
        executorService.shutdownNow();

        try {
            if (!executorService.awaitTermination(100, TimeUnit.MICROSECONDS)) {
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void printTestResults() {
        Set<FormPair> forms = getData().getForLearningDirection(getLearningProperties().getDirection());
        for (FormPair formPair : forms) {
            Evaluation evaluation = getGrammar().evaluate(formPair, true, getLearningProperties().getEvaluationNoise());
            ViolatedCandidate lCandidate = evaluation.getWinner();
            System.out.println(lCandidate.toString());
        }
        Hierarchy grammarHierarchy = getGrammar().getHierarchy();
        grammarHierarchy.printContents();
    }

    private int calculateResetCounterEvery(int numEvaluations) {
        double asNumber = ConfigFactory.load().getDouble("stats.showErrorEvery");
        if (asNumber > 1) {
            return (int) asNumber;
        } else if (asNumber > 0) {
            return (int) (asNumber * numEvaluations);
        } else {
            return numEvaluations+1;
        }
    }

}
