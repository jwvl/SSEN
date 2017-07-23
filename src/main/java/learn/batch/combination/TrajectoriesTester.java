package learn.batch.combination;

import com.google.common.collect.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import constraints.Constraint;
import constraints.hierarchy.analysis.ConstraintOrderMap;
import constraints.hierarchy.analysis.Poset;
import constraints.hierarchy.analysis.PosetBuilder;
import constraints.hierarchy.reimpl.Hierarchy;
import eval.sample.AbstractSampler;
import eval.sample.GaussianXORSampler;
import forms.Form;
import forms.FormPair;
import forms.morphosyntax.SemSynForm;
import grammar.Grammar;
import grammar.tools.GrammarTester;
import graph.Direction;
import io.riverplot.RiverPlotOutput;
import learn.batch.LearningProperties;
import learn.batch.LearningTrajectory;
import learn.data.DistributionErrorTable;
import learn.data.PairDistribution;
import learn.data.SinglesFilter;
import learn.stats.results.ResultsTable;
import simulate.analysis.CandidateMappingTable;
import simulate.analysis.statistics.CandidateSetCollector;
import simulate.analysis.statistics.HierarchyComparer;
import simulate.analysis.visualize.GoogleSankey;
import simulate.french.sixlevel.analyze.SimpleResultsTable;
import simulate.french.sixlevel.data.PfcData;
import util.collections.ConfusionMatrix;
import util.collections.Couple;
import util.debug.Stopwatch;

import java.util.*;

/**
 * Created by janwillem on 31/07/16.
 */
public class TrajectoriesTester {
    private final Config config = ConfigFactory.load();
    private final LearningPropertyCombinations combinations;
    private final Grammar grammar;
    private final PfcData pfcData;
    private final PairDistribution trainDistribution;
    private final PairDistribution testDistribution;
    private final ResultsTable resultsTable;
    private final Map<UUID, Hierarchy> finalHierarchies;
    private final Map<UUID, Double> errorRates;
    private final Table<SemSynForm, UUID, CandidateMappingTable> liaisonCandidates;
    private final static boolean compareLiaisons = false;
    private final static boolean printSankeyDiagrams = false;
    private final static boolean calculateSimilarities = false;
    private final static boolean printCandidateSets = false;
    private final static boolean collectPairDistributions = false;
    private final static double successThreshold = 0.05;
    private final int numDataToTest;

    public TrajectoriesTester(LearningPropertyCombinations combinations, Grammar grammar, PfcData pfcData) {
        this.pfcData = pfcData;
        PairDistribution pairDistribution = pfcData.getPairDistribution().filter(new SinglesFilter());;
        this.combinations = combinations;
        this.grammar = grammar;

        double testFraction = config.getDouble("learning.testDataFraction");
        numDataToTest = config.getInt("stats.numDataToTest");
        Couple<PairDistribution> testTrain = pairDistribution.splitToTestAndTraining(testFraction);
        this.testDistribution = testTrain.getLeft();
        this.trainDistribution = testTrain.getRight();
        resultsTable = new ResultsTable();
        finalHierarchies = new HashMap<>();
        errorRates = new HashMap<>();
        liaisonCandidates = HashBasedTable.create();
    }

    public void testAndWrite(String name, int numEvaluations, int numTests, int numThreads, String outputPath) {
        AbstractSampler nearZeroSampler = GaussianXORSampler.createInstance(0.00000001);
        DistributionErrorTable errorTable = new DistributionErrorTable();
        SimpleResultsTable simpleResultsTable = new SimpleResultsTable();
        PairDistribution pfcDistribution = pfcData.getLiaisonDistribution();
        for (LearningProperties learningProperties : combinations) {
            double evaluationNoise = learningProperties.getEvaluationNoise();
            for (int i = 0; i < numTests; i++) {
                LearningTrajectory trajectory = new LearningTrajectory(grammar, trainDistribution, numEvaluations);
                trajectory.launch(numThreads);
                Stopwatch.reportElapsedTime("Finished training in ", true);
                Map<Integer, Double> errorRates = trajectory.getErrorRates();
                double error = GrammarTester.testGrammarOnLearningData(grammar, trainDistribution, numDataToTest, evaluationNoise);
                System.out.println("Total training error at run " + i +" : " +error);
                if (testDistribution != null) {
                    double testError = GrammarTester.testGrammarOnLearningData(grammar, testDistribution, numDataToTest, evaluationNoise);
                    System.out.println("Total test error at run: "+ i + ": "+testError);
                }
                System.out.println("Min training error: " + trainDistribution.calculateExpectedError());
                if (collectPairDistributions) {
                    //System.out.println("As pair distribution:");
                    PairDistribution resultingDistribution = GrammarTester.toPairDistribution(grammar, trainDistribution, 100, evaluationNoise);
                    System.out.println("Created error distribution on training set");
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
                    resultsTable.appendDatum("evaluationNoise", String.valueOf(learningProperties.getEvaluationNoise()));
                    resultsTable.appendDatum("initPlasticity", "" + learningProperties.getInitialPlasticity());
                    resultsTable.appendDatum("plasticityDecay", "" + learningProperties.getPlasticityDecay());
                    resultsTable.appendDatum("numEpochs", "" + learningProperties.getPlasticityEpochs());
                    resultsTable.appendDatum("updateAlgorithm", "" + learningProperties.getUpdateAlgorithm().toString());
                    resultsTable.appendDatum("errorRate", "" + iError);
                }

                finalHierarchies.put(trajectory.getUuid(), grammar.getHierarchy().copy());
                this.errorRates.put(trajectory.getUuid(), error);
                Set<SemSynForm> toTest = pfcData.getLiaisonSsfs();
                Set<Form> liaisonInputsAsForms = Sets.newHashSet();
                for (SemSynForm ssf: toTest) {
                    liaisonInputsAsForms.add(ssf);
                }
                if (compareLiaisons) {

                    PairDistribution liaisonDistribution = GrammarTester.toPairDistribution(grammar,liaisonInputsAsForms, Direction.RIGHT, 25,evaluationNoise);
                    ConfusionMatrix grammarConfusions = ConfusionMatrix.fromPairDistribution(liaisonDistribution);
                    double rmse = ConfusionMatrix.rmse(pfcData.getLiaisonConfusions(),grammarConfusions);
                    System.out.println("RMSE for liaison items: " + rmse);
                }

                int numLiaisonRiverPlots = config.getInt("stats.numLiaisonRiverPlots");
                System.out.print("Creating liaison candidate sets...");
                if (numLiaisonRiverPlots > 0) {
                    for (SemSynForm ssf : pfcData.getLiaisonSsfs()) {
                        FormPair input = FormPair.createUnlabeled(ssf, Direction.RIGHT);
                        CandidateMappingTable table = GrammarTester.getCandidateMappingTable(grammar, input, numLiaisonRiverPlots, evaluationNoise);
                        liaisonCandidates.put(ssf,trajectory.getUuid(),table);
                    }
                }
                System.out.println("done");
                double errorOnLiaisons = GrammarTester.testGrammarOnLearningData(grammar,pfcDistribution,5000,evaluationNoise);
                System.out.println("Error on liaison distribution: " +errorOnLiaisons);
                simpleResultsTable.addResult(("Grammar"+i),"LiaisonDistributionError",errorOnLiaisons);
                double[] results = pfcData.testGrammar(grammar, 10000, evaluationNoise);
                simpleResultsTable.addResult(("Grammar"+i),"Possible",results[0]);
                simpleResultsTable.addResult(("Grammar"+i),"NotInData",results[1]);
                simpleResultsTable.addResult(("Grammar"+i),"inData",results[2]);
                simpleResultsTable.addResult(("Grammar"+i),"Liaising",results[3]);
                grammar.resetConstraints(100.0);

            }
        }
        //printPoset();
        if (finalHierarchies.size() > 2 && calculateSimilarities) {
            HierarchyComparer comparer = HierarchyComparer.build(finalHierarchies, grammar, trainDistribution, Direction.RIGHT);
            comparer.writeToFile(outputPath+"/hierarchyDistances.txt");
        }

        resultsTable.saveToFile(outputPath+"/simulationResults.txt");

        if (printSankeyDiagrams) {
            tablesToRiverplots(getSuccesfulHierarchies(),outputPath+"/riverplot-s");
            tablesToRiverplots(getFailedHierarchies(),outputPath+"/riverplot-f");

        }

        if (printCandidateSets) {
            List<FormPair> formPairs = Lists.newArrayList(trainDistribution.getKeys());
            CandidateSetCollector candidateSetCollector = new CandidateSetCollector(formPairs, Direction.RIGHT);
            for (Hierarchy hierarchy: finalHierarchies.values()) {
                grammar.setHierarchy(hierarchy);
                candidateSetCollector.addTests(grammar,1,0.0000001);
            }
            System.out.println("Collected results:");
            System.out.println(candidateSetCollector.getResults());
        }

        if (collectPairDistributions) {
            System.out.println(errorTable.toTableString());
        }

        System.out.println(simpleResultsTable.printRows(true));


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
        if (finalHierarchies.size() > 0) {
            ConstraintOrderMap constraintOrderMap = new ConstraintOrderMap(finalHierarchies.values());
            Poset<Constraint> poset = PosetBuilder.buildPoset(finalHierarchies.get(0), constraintOrderMap);
            System.out.println(poset);
        }
    }

    public Map<UUID, Hierarchy> getSuccesfulHierarchies() {
        Map<UUID, Hierarchy> result = new HashMap<>();
        for (UUID key: finalHierarchies.keySet()) {
            if (errorRates.get(key) < successThreshold) {
                result.put(key, finalHierarchies.get(key));
            }
        }
        return result;
    }


    public Table<SemSynForm, UUID, CandidateMappingTable> getLiaisonCandidates() {
        return liaisonCandidates;
    }

    public Hierarchy getBestHierarchy() {
        double highest = 100;
        Hierarchy best = null;
        for (UUID key: errorRates.keySet()) {
            double nextScore = errorRates.get(key);
            if (errorRates.get(key) < highest) {
                best = finalHierarchies.get(key);
                highest = nextScore;
            }
        }
        return best;
    }

    public Map<UUID, Hierarchy> getFailedHierarchies() {
        Map<UUID, Hierarchy> result = new HashMap<>();
        for (UUID key: finalHierarchies.keySet()) {
            if (errorRates.get(key) > successThreshold) {
                result.put(key, finalHierarchies.get(key));
            }
        }
        return result;
    }

    private void createSankeyDiagrams(Grammar grammar) {
        Map<FormPair,CandidateMappingTable> tables = hierarchiesToMap(grammar, finalHierarchies);
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

    private void saveHierarchies(Map<UUID, Hierarchy> uuidHierarchyMap ) {
        // TODO implement
    }

}
