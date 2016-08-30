package learn.batch.combination;

import constraints.Constraint;
import constraints.hierarchy.analysis.ConstraintOrderMap;
import constraints.hierarchy.analysis.Poset;
import constraints.hierarchy.analysis.PosetBuilder;
import constraints.hierarchy.reimpl.Hierarchy;
import eval.sample.AbstractSampler;
import eval.sample.GaussianXORSampler;
import forms.FormPair;
import grammar.Grammar;
import grammar.tools.GrammarTester;
import learn.PairDistribution;
import learn.batch.LearningProperties;
import learn.batch.LearningTrajectory;
import learn.stats.results.ResultsTable;
import simulate.analysis.CandidateMappingTable;
import simulate.analysis.visualize.GoogleSankey;
import util.debug.Stopwatch;
import util.time.DateString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by janwillem on 31/07/16.
 */
public class TrajectoriesTester {
    private final LearningPropertyCombinations combinations;
    private final Grammar grammar;
    private final PairDistribution pairDistribution;
    private final ResultsTable resultsTable;
    private final List<Hierarchy> resultingHierarchies;

    public TrajectoriesTester(LearningPropertyCombinations combinations, Grammar grammar, PairDistribution pairDistribution) {
        this.combinations = combinations;
        this.grammar = grammar;
        this.pairDistribution = pairDistribution;
        resultsTable = new ResultsTable();
        resultingHierarchies = new ArrayList<>();
    }

    public void testAndWrite(String name, int numEvaluations, int numTests, int numThreads) {
        AbstractSampler nearZeroSampler = GaussianXORSampler.createInstance(0.00000001);
        for (LearningProperties learningProperties : combinations) {
            for (int i = 0; i < numTests; i++) {
                LearningTrajectory trajectory = new LearningTrajectory(grammar, pairDistribution, numEvaluations);
                trajectory.launch(numThreads);
                Stopwatch.reportElapsedTime("Finished testing in ", true);
                Map<Integer,Double> errorRates = trajectory.getErrorRates();
                double error = GrammarTester.testGrammarOnLearningData(grammar, pairDistribution, 200, 1.0);

                for (Integer iData: errorRates.keySet()) {
                    double iError = errorRates.get(iData);
                    resultsTable.appendRow();
                    resultsTable.appendDatum("name", name);
                    resultsTable.appendDatum("uuid",trajectory.getUuid().toString());
                    resultsTable.appendDatum("atDatum", "" + iData);
                    resultsTable.appendDatum("numData", ""+numEvaluations);
                    resultsTable.appendDatum("evaluationNoise", "1.0");
                    resultsTable.appendDatum("initPlasticity", "" + learningProperties.getInitialPlasticity());
                    resultsTable.appendDatum("plasticityDecay", "" + learningProperties.getPlasticityDecay());
                    resultsTable.appendDatum("numEpochs", "" + learningProperties.getPlasticityEpochs());
                    resultsTable.appendDatum("updateAlgorithm", "" + learningProperties.getUpdateAlgorithm().toString());
                    resultsTable.appendDatum("errorRate", "" + iError );
                }


                if (error < 0.05) {
                    resultingHierarchies.add(grammar.getHierarchy().sample(nearZeroSampler));
                }
                CandidateMappingTable[] tables = new CandidateMappingTable[pairDistribution.getNumPairs()];
                int count = 0;
                for (FormPair formPair: pairDistribution.getKeys()) {
                    tables[count] = GrammarTester.getCandidateMappingTable(grammar, formPair, 100, 1.0);
                    System.out.println("Candidate mappings for pair " + formPair);
                    System.out.println(GoogleSankey.toHtml(tables[count++]));
                    System.out.println();
                }
                grammar.resetConstraints(100.0);
            }
        }
        printPoset();

        resultsTable.saveToFile("outputs/simulationResults-" + DateString.getShortDateString());
    }

    private void printPoset() {
        if (resultingHierarchies.size() > 0) {
            ConstraintOrderMap constraintOrderMap = new ConstraintOrderMap(resultingHierarchies);
            Poset<Constraint> poset = PosetBuilder.buildPoset(resultingHierarchies.get(0), constraintOrderMap);
            System.out.println(poset);
        }
    }

}
