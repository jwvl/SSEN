package learn.batch;

import grammar.Grammar;
import learn.PairDistribution;

/**
 * Created by janwillem on 30/08/16.
 */
public class RandomLearnerTester {
    private final Grammar grammar;
    private final PairDistribution pairDistribution;

    public RandomLearnerTester(Grammar grammar, PairDistribution pairDistribution, int numEvaluations) {
        this.grammar = grammar;
        this.pairDistribution = pairDistribution;

    }
}
