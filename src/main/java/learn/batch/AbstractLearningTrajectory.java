/**
 *
 */
package learn.batch;

import grammar.Grammar;
import learn.data.LearningData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jwvl
 * @date 26/03/2016
 */
public abstract class AbstractLearningTrajectory {
    private final Grammar grammar;
    private final LearningData data;
    private final LearningProperties learningProperties;
    private final UUID uuid;
    private final Map<Integer,Double> errorRates;

    public Grammar getGrammar() {
        return grammar;
    }

    public LearningData getData() {
        return data;
    }

    public LearningProperties getLearningProperties() {
        return learningProperties;
    }


    public AbstractLearningTrajectory(Grammar grammar, LearningData data, LearningProperties learningProperties) {
        this.grammar = grammar;
        this.data = data;
        this.learningProperties = learningProperties;
        this.uuid = UUID.randomUUID();
        this.errorRates = new HashMap<Integer,Double>();
    }

    public AbstractLearningTrajectory(Grammar grammar, LearningData data) {
        this(grammar, data, grammar.getDefaultLearningProperties());
    }

    public void launch(int numThreads) {
        ExecutorService executorService;
        if (numThreads <= 1) {
            executorService = Executors.newSingleThreadExecutor();
        } else {
            executorService = Executors.newFixedThreadPool(numThreads);
        }
        run(executorService);
        executorService.shutdown();
    }

    public UUID getUuid() {
        return uuid;
    }

    public abstract void run(ExecutorService executorService);

    public void addErrorRate(int learningStep, double error) {
        errorRates.put(learningStep,error);
    }


    public Map<Integer, Double> getErrorRates() {
        return errorRates;
    }

}
