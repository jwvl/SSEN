package learn.batch.combination;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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
import graph.Direction;
import io.riverplot.RiverPlotOutput;
import learn.batch.LearningProperties;
import learn.batch.LearningTrajectory;
import learn.data.DistributionErrorTable;
import learn.data.PairDistribution;
import learn.stats.results.ResultsTable;
import simulate.analysis.CandidateMappingTable;
import simulate.analysis.statistics.CandidateSetCollector;
import simulate.analysis.statistics.HierarchyComparer;
import simulate.analysis.visualize.GoogleSankey;
import util.collections.Couple;
import util.debug.Stopwatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by janwillem on 31/07/16.
 */
public class TrajectoriesTester {
    private final LearningPropertyCombinations combinations;
    private final Grammar grammar;
    private final PairDistribution trainDistribution;
    private final PairDistribution testDistribution;
    private final ResultsTable resultsTable;
    private final Map<UUID, Hierarchy> successfulHierarchies;
    private final Map<UUID, Hierarchy> failedHierarchies;
    private final static boolean printSankeyDiagrams = false;
    private final static boolean calculateSimilarities = false;
    private final static boolean printCandidateSets = false;
    private final static boolean collectPairDistributions = true;
    private final static double successThreshold = 0.05;
    private final int numDataToTest;

    public TrajectoriesTester(LearningPropertyCombinations combinations, Grammar grammar, PairDistribution pairDistribution) {
        this.combinations = combinations;
        this.grammar = grammar;
        Config config = ConfigFactory.load();
        double testFraction = config.getDouble("learning.testDataFraction");
        numDataToTest = config.getInt("stats.numDataToTest");
        Couple<PairDistribution> testTrain = pairDistribution.splitToTestAndTraining(testFraction);
        this.testDistribution = testTrain.getLeft();
        this.trainDistribution = testTrain.getRight();
        resultsTable = new ResultsTable();
        successfulHierarchies = new HashMap<UUID, Hierarchy>();
        failedHierarchies = new HashMap<UUID, Hierarchy>();
    }

    public void testAndWrite(String name, int numEvaluations, int numTests, int numThreads, String outputPath) {
        AbstractSampler nearZeroSampler = GaussianXORSampler.createInstance(0.00000001);
        DistributionErrorTable errorTable = new DistributionErrorTable();
        for (LearningProperties learningProperties : combinations) {
            for (int i = 0; i < numTests; i++) {
                LearningTrajectory trajectory = new LearningTrajectory(grammar, trainDistribution, numEvaluations);
                trajectory.launch(numThreads);
                Stopwatch.reportElapsedTime("Finished training in ", true);
                Map<Integer, Double> errorRates = trajectory.getErrorRates();
                double error = GrammarTester.testGrammarOnLearningData(grammar, trainDistribution, numDataToTest, 1.0);
                System.out.println("Total training error: " +error);
                if (testDistribution != null) {
                    double testError = GrammarTester.testGrammarOnLearningData(grammar, testDistribution, numDataToTest, 1.0);
                    System.out.println("Total test error: " +testError);
                }
                System.out.println("Min training error: " + trainDistribution.calculateExpectedError());
                if (collectPairDistributions) {
                    //System.out.println("As pair distribution:");
                    PairDistribution resultingDistribution = GrammarTester.toPairDistribution(grammar, trainDistribution, 100, 1.0);
                    errorTable.addLearner(trajectory.getUuid(),trainDistribution,resultingDistribution);
                    //resultingDistribution.printAsList();
                }
                for (Integer iData : errorRates.keySet()) {
                    double iError = errorRates.get(iData);
                    resultsTable.appendRow();
                    resultsTable.appendDatum("name", name);
                    resultsTable.appendDatum("uuid", trajectory.getUuid().toString());
                    resultsTable.appendDatum("atDatum", "" + iData);
                    resultsTable.appendDatum("numData", "" + numEvaluations);
                    resultsTable.appendDatum("evaluationNoise", "1.0");
                    resultsTable.appendDatum("initPlasticity", "" + learningProperties.getInitialPlasticity());
                    resultsTable.appendDatum("plasticityDecay", "" + learningProperties.getPlasticityDecay());
                    resultsTable.appendDatum("numEpochs", "" + learningProperties.getPlasticityEpochs());
                    resultsTable.appendDatum("updateAlgorithm", "" + learningProperties.getUpdateAlgorithm().toString());
                    resultsTable.appendDatum("errorRate", "" + iError);
                }


                if (error < successThreshold) {
                    successfulHierarchies.put(trajectory.getUuid(), grammar.getHierarchy().copy());
                } else {
                    failedHierarchies.put(trajectory.getUuid(), grammar.getHierarchy().copy());
                }

                grammar.resetConstraints(100.0);
            }
        }
        //printPoset();
        if (successfulHierarchies.size() > 2 && calculateSimilarities) {
            HierarchyComparer comparer = HierarchyComparer.build(successfulHierarchies, grammar, trainDistribution, Direction.RIGHT);
            comparer.writeToFile(outputPath+"/hierarchyDistances.txt");
        }

        resultsTable.saveToFile(outputPath+"/simulationResults.txt");

        if (printSankeyDiagrams) {
            //createSankeyDiagrams(grammar);
            tablesToRiverplots(successfulHierarchies,outputPath+"/riverplot-s");
            tablesToRiverplots(failedHierarchies,outputPath+"/riverplot-f");

        }

        if (printCandidateSets) {
            List<FormPair> formPairs = Lists.newArrayList(trainDistribution.getKeys());
            CandidateSetCollector candidateSetCollector = new CandidateSetCollector(formPairs, Direction.RIGHT);
            for (Hierarchy hierarchy: successfulHierarchies.values()) {
                grammar.setHierarchy(hierarchy);
                candidateSetCollector.addTests(grammar,1,0.0000001);
            }
            System.out.println("Collected results:");
            System.out.println(candidateSetCollector.getResults());
        }

        if (collectPairDistributions) {
            System.out.println(errorTable.toTableString());
        }
    }

    private void tablesToRiverplots(Map<UUID,Hierarchy> hierarchies,String prefix) {
        Map<FormPair,CandidateMappingTable> successTables = hierarchiesToMap(grammar,hierarchies);
        List<RiverPlotOutput> riverPlotOutputs = Lists.newArrayList();
        for (FormPair formPair: successTables.keySet()) {
            CandidateMappingTable table = successTables.get(formPair);
            RiverPlotOutput riverPlotOutput = new RiverPlotOutput(formPair.left(), table, 0.05);
            riverPlotOutputs.add(riverPlotOutput);
        }
        RiverPlotOutput.writeNodeAndEdgeFiles(prefix,riverPlotOutputs);
    }

    private void printPoset() {
        if (successfulHierarchies.size() > 0) {
            ConstraintOrderMap constraintOrderMap = new ConstraintOrderMap(successfulHierarchies.values());
            Poset<Constraint> poset = PosetBuilder.buildPoset(successfulHierarchies.get(0), constraintOrderMap);
            System.out.println(poset);
        }
    }

    public Map<UUID, Hierarchy> getSuccessfulHierarchies() {
        return successfulHierarchies;
    }

    private void createSankeyDiagrams(Grammar grammar) {
        Map<FormPair,CandidateMappingTable> tables = hierarchiesToMap(grammar, successfulHierarchies);
        for (FormPair formPair: tables.keySet()) {
            System.out.println("Candidate mappings for pair " + formPair);
            System.out.println(GoogleSankey.toHtml(tables.get(formPair)));
            System.out.println();
        }
    }

    private Map<FormPair,CandidateMappingTable> hierarchiesToMap(Grammar grammar, Map<UUID,Hierarchy> uuidHierarchyMap) {
        Map<FormPair,CandidateMappingTable> tables = Maps.newHashMap();
        List<FormPair> formPairs = Lists.newArrayList(trainDistribution.getForLearningDirection(Direction.RIGHT));
        for (int i = 0; i < formPairs.size(); i++) {
            FormPair formPair = formPairs.get(i);
            CandidateMappingTable table = CandidateMappingTable.createNew();
            for (Hierarchy hierarchy : uuidHierarchyMap.values()) {
                grammar.setHierarchy(hierarchy);
                table = table.mergeWith(GrammarTester.getCandidateMappingTable(grammar, formPair, 100, 1.0));
            }
            tables.put(formPair, table);
        }
        return tables;
    }

}
