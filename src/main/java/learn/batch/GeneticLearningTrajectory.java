/**
 *
 */
package learn.batch;

import eval.sample.GaussianXORSampler;
import forms.FormPair;
import grammar.dynamic.DynamicNetworkGrammar;
import learn.data.LearningData;
import learn.genetic.CandidatePool;
import learn.genetic.ConHypothesis;
import ranking.GrammarHierarchy;
import ranking.Hierarchy;
import ranking.SampledHierarchy;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author jwvl
 * @date 30/09/2015
 */
public class GeneticLearningTrajectory extends AbstractLearningTrajectory {

    private DynamicNetworkGrammar grammar;
    private LearningData data;
    private int numEvaluations;
    private static boolean TEST_RESULTS = true;
    private int populationSize = 10;
    private CandidatePool candidatePool;
    private LearningProperties learningProperties;
    private int triesPerPool = 1000;

    /**
     * @param grammar
     * @param data
     */
    public GeneticLearningTrajectory(DynamicNetworkGrammar grammar,
                                     LearningData data, int numEvaluations, LearningProperties learningProperties) {
        super(grammar, data);
        this.grammar = grammar;
        this.data = data;
        this.candidatePool = CandidatePool.createNew(populationSize);
        this.numEvaluations = numEvaluations;
        this.learningProperties = learningProperties;
    }

    @Override
    public void run(ExecutorService executorService) {
        int numEvaluated = 0;
        double evaluationNoise = learningProperties.getEvaluationNoise();


        while (numEvaluated < numEvaluations && data.hasNext()) {
            CompletionService<Boolean> completionService =
                    new ExecutorCompletionService<Boolean>(executorService);
            int poolSize = candidatePool.size();
            List<ConHypothesis> hypothesisList = candidatePool.getMembers();
            for (int i = 0; i < triesPerPool; i++) {
                ConHypothesis nextHypothesis = hypothesisList.get(i % poolSize);
                GrammarHierarchy hierarchy = nextHypothesis.getCon();
                SampledHierarchy sampled = new SampledHierarchy(hierarchy, GaussianXORSampler.createInstance(evaluationNoise));
                FormPair randomPair = getData().next();
                SingleEvaluationStep singleEvaluationStep = SingleEvaluationStep.getInstance(learningProperties, grammar, sampled, randomPair);
                completionService.submit(singleEvaluationStep);
            }

            for (int i = 0; i < triesPerPool; i++) {
                try {
                    Future<Boolean> resultFuture = completionService.take();
                    boolean success = resultFuture.get();
                    hypothesisList.get(i % poolSize).addEvaluation(success);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                numEvaluated++;
            }

            updateCandidatePool(grammar.getRankedCon());

        }
        executorService.shutdown();

    }

    private void updateCandidatePool(Hierarchy hierarchy) {
        candidatePool.toNextGeneration(hierarchy);
    }
}
