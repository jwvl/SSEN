/**
 *
 */
package learn.batch;

import constraints.hierarchy.reimpl.Hierarchy;
import forms.FormPair;
import forms.GraphForm;
import grammar.dynamic.DynamicNetworkEvaluation;
import grammar.dynamic.DynamicNetworkGrammar;
import graph.Direction;
import learn.ViolatedCandidate;

import java.util.concurrent.Callable;

/**
 * @author jwvl
 * @date 25/03/2016
 */
public class SingleEvaluationStep implements Callable<Boolean> {
    private final Hierarchy sampledHierarchy;
    private final FormPair learningDatum;
    private final LearningProperties properties;
    private final DynamicNetworkGrammar grammar;

    /**
     * @param properties
     * @param grammar
     * @param learningDatum
     */
    private SingleEvaluationStep(LearningProperties properties, DynamicNetworkGrammar grammar, Hierarchy sampledHierarchy, FormPair learningDatum) {
        this.sampledHierarchy = sampledHierarchy;
        this.learningDatum = learningDatum;
        this.properties = properties;
        this.grammar = grammar;
    }

    public static SingleEvaluationStep getInstance(LearningProperties learningProperties, DynamicNetworkGrammar grammar, Hierarchy sampledHierarchy, FormPair learningDatum) {
        return new SingleEvaluationStep(learningProperties, grammar, sampledHierarchy, learningDatum);
    }

    public Boolean call() throws Exception {
        FormPair testPair = createTestPair();
        DynamicNetworkEvaluation evaluation = new DynamicNetworkEvaluation(grammar, sampledHierarchy);
        evaluation.setStartAndEndForm(testPair);
        evaluation.run();
        ViolatedCandidate lCandidate = evaluation.getWinner();
        return (correspondsWithLearningDatum(lCandidate));
    }

    /**
     * @param lCandidate
     * @return
     */
    private boolean correspondsWithLearningDatum(ViolatedCandidate lCandidate) {
        return lCandidate.getCandidate().containsForm(learningDatum.right());
    }

    /**
     * @return
     */
    private FormPair createTestPair() {
        Direction direction = properties.getDirection();
        if (direction == Direction.RIGHT) {
            return FormPair.of(learningDatum.left(), GraphForm.getSinkInstance());
        } else if (direction == Direction.LEFT) {
            return FormPair.of(GraphForm.getSourceInstance(), learningDatum.right());
        }

        return null;
    }

}
