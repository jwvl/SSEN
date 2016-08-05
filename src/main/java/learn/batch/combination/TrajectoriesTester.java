package learn.batch.combination;

import grammar.Grammar;
import grammar.tools.GrammarTester;
import learn.PairDistribution;
import learn.batch.LearningProperties;
import learn.batch.LearningTrajectory;
import learn.stats.results.ResultsTable;
import util.debug.Stopwatch;
import util.time.DateString;

/**
 * Created by janwillem on 31/07/16.
 */
public class TrajectoriesTester {
    private final LearningPropertyCombinations combinations;
    private final Grammar grammar;
    private final PairDistribution pairDistribution;
    private final ResultsTable resultsTable;

    public TrajectoriesTester(LearningPropertyCombinations combinations, Grammar grammar, PairDistribution pairDistribution) {
        this.combinations = combinations;
        this.grammar = grammar;
        this.pairDistribution = pairDistribution;
        resultsTable = new ResultsTable();
    }

    public void testAndWrite(String name, int numEvaluations, int numTests, int numThreads) {
        for (LearningProperties learningProperties: combinations) {
        for (int i=0; i < numTests; i++) {
            LearningTrajectory trajectory = new LearningTrajectory(grammar, pairDistribution, numEvaluations);
            trajectory.launch(numThreads);
            Stopwatch.reportElapsedTime("Finished testing in ", true);
            double error = GrammarTester.testGrammarOnLearningData(grammar, pairDistribution,200,1.0);

            resultsTable.appendRow();
            resultsTable.appendDatum("name",name);
            resultsTable.appendDatum("numData",""+numEvaluations);
            resultsTable.appendDatum("evaluationNoise", "1.0");
            resultsTable.appendDatum("initPlasticity",""+learningProperties.getInitialPlasticity());
            resultsTable.appendDatum("plasticityDecay",""+learningProperties.getPlasticityDecay());
            resultsTable.appendDatum("numEpochs",""+learningProperties.getPlasticityEpochs());
            resultsTable.appendDatum("updateAlgorithm",""+learningProperties.getUpdateAlgorithm().toString());
            resultsTable.appendDatum("errorRate",""+error);
            grammar.getHierarchy().printContents();
            grammar.resetConstraints(100.0);
        }}

        resultsTable.saveToFile("outputs/simulationResults-"+ DateString.getShortDateString());
    }
}
