/**
 *
 */
package learn.batch;

import com.typesafe.config.ConfigFactory;
import eval.Evaluation;
import forms.FormPair;
import grammar.Grammar;
import learn.ViolatedCandidate;
import learn.data.LearningData;
import learn.stats.ErrorCounter;
import ranking.GrammarHierarchy;
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
        ExecutorCompletionService<Boolean> completionService =
                new ExecutorCompletionService<Boolean>(executorService);
        int counter = 0;
        int epochSize = numEvaluations / getLearningProperties().getPlasticityEpochs();
        while (counter < numEvaluations && getData().hasNext()) {
            FormPair randomPair = getData().next();
            LearningStep learningStep = LearningStep.getInstance(getLearningProperties(), getGrammar(), randomPair, plasticity);
            completionService.submit(learningStep);
            if (counter % epochSize == 0) {
                plasticity *= getLearningProperties().getPlasticityDecay();
            }
            counter++;
        }

        while (numEvaluated < numEvaluations) {
            try {
                Future<Boolean> resultFuture = completionService.take();
                Boolean success = resultFuture.get();
                numEvaluated++;
                boolean error = !success;
                shortCounter.increaseCount(error);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                numEvaluated = numEvaluations;
            }

            if (shortCounter.getTotal() >= resetCounterEvery) {
                String intermediateResult = String.format("Current error rate: %s (%s pct)", shortCounter.getErrorAsRatio(), shortCounter.getErrorAsPercentage());
                System.out.println(intermediateResult);
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
        GrammarHierarchy grammarHierarchy = getGrammar().getRankedCon();
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
