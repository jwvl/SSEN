package learn.batch;

import constraints.hierarchy.reimpl.Hierarchy;
import grammar.Grammar;
import learn.data.PairDistribution;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by janwillem on 30/08/16.
 */
public class RandomLearnerTester {
    private final Grammar grammar;
    private final PairDistribution pairDistribution;
    private final List<Hierarchy> winningHierarchies;
    private final int numEvaluations;

    public RandomLearnerTester(Grammar grammar, PairDistribution pairDistribution, int numEvaluations) {
        this.grammar = grammar;
        this.pairDistribution = pairDistribution;
        winningHierarchies = new ArrayList<>();
        this.numEvaluations = numEvaluations;
    }

    public void testAndPrint(int numTests) {
        for (int i=0; i < numTests; i++) {
            RandomLearningTrajectory randomLearningTrajectory = new RandomLearningTrajectory(grammar, pairDistribution, null, numEvaluations);
            randomLearningTrajectory.run(null);
            boolean foundWinning = randomLearningTrajectory.isWinningHierarchyFound();
            if (foundWinning) {
                System.out.println("Found winning hierarchy by chance for learner "+i);
            }
        }

    }
}
