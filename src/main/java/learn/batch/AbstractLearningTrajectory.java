/**
 *
 */
package learn.batch;

import grammar.Grammar;
import learn.data.LearningData;

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
    }

    public AbstractLearningTrajectory(Grammar grammar, LearningData data) {
        this.grammar = grammar;
        this.data = data;
        this.learningProperties = grammar.getLearningProperties();
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

    public abstract void run(ExecutorService executorService);
}
