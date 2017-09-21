/**
 *
 */
package simulate.french.sixlevel;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.google.common.io.Resources;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import constraints.hierarchy.reimpl.Hierarchy;
import eval.Evaluation;
import forms.Form;
import forms.FormPair;
import forms.morphosyntax.*;
import forms.phon.flat.PhoneticForm;
import forms.primitives.segment.Phone;
import gen.alignment.MorphemePhoneAligner;
import gen.alignment.MorphemePhoneAlignment;
import gen.alignment.SubstringDatabank;
import gen.constrain.UfConstrainer;
import gen.mapping.FormMapping;
import gen.mapping.SubCandidateSet;
import gen.subgen.SubGen;
import grammar.dynamic.DynamicNetworkGrammar;
import grammar.levels.Level;
import grammar.levels.predefined.BiPhonSix;
import grammar.subgraph.CandidateSpaces;
import grammar.tools.GrammarTester;
import graph.Direction;
import io.MyStringTable;
import io.candidates.CandidateSpacesNodeList;
import io.candidates.CandidateSpacesToTables;
import io.riverplot.RiverPlotOutput;
import io.tableau.SimpleTableau;
import io.tableau.SimpleTableauBuilder;
import io.utils.PathUtils;
import learn.ViolatedCandidate;
import learn.batch.RandomLearnerTester;
import learn.batch.combination.LearningPropertyCombinations;
import learn.batch.combination.TrajectoriesTester;
import learn.data.PairDistribution;
import learn.data.SinglesFilter;
import simulate.analysis.CandidateMappingTable;
import simulate.french.sixlevel.data.PfcCooccurrences;
import simulate.french.sixlevel.data.PfcData;
import simulate.french.sixlevel.helpers.LexicalHypothesisRepository;
import simulate.french.sixlevel.helpers.SettingsMap;
import simulate.french.sixlevel.subgens.*;
import util.collections.StringMultimap;
import util.debug.Timer;
import util.string.ngraph.ByteNGraphMap;
import util.time.DateString;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author jwvl
 * @date Dec 17, 2014
 *
 * The main class for performing the simulations described in Chapter 5 of my thesis.
 * It has grown a bit messy and could do with some refactoring.
 */
public class SixLevelFrenchDynamic {

    private static SubstringDatabank lcsData;
    private static ByteNGraphMap bigraphs;
    private static Config config = ConfigFactory.load();
    private static int numRuns = config.getInt("learning.numRuns");
    private static boolean saveLexiconToFile = config.getBoolean("lexicon.saveToFile");
    private static boolean getLexiconFromFile = config.getBoolean("lexicon.constructFromFile");
    private static String dateString = DateString.getShortDateString();

    public static void main(String[] args) throws IOException {

        String outputPath = "outputs/"+dateString;
        boolean couldCreateDir = new File(outputPath).mkdir();
        if (!couldCreateDir) {
            System.err.println("Couldn't create directory " +outputPath);
            System.exit(0);
        }
        copyApplicationConf(outputPath);


        // 0 Read data.phones and properties
        Config config = ConfigFactory.load();
        int numThreads = config.getInt("system.numThreads");

        int mappingCacheSize = config.getInt("grammar.mappingCacheSize");
        int numEvaluations = config.getInt("learning.numEvaluations");

        String phoneInfoFileName = config.getString("files.phoneInfo");
        String dataFilePath = config.getString("files.learningData");
        String dataFileName = PathUtils.getFilenameFromPath(dataFilePath);

        MyStringTable phonesTable = MyStringTable.fromFile(phoneInfoFileName, true, ",");
        String sonorityColumn = config.getString("implementation.sonorityType");
        Phone.getFromStringTable(phonesTable, "Phone", "String", sonorityColumn);


        // 1 Create grammar with levels.
        Level semF_level = BiPhonSix.getSemSynFormLevel();
        Level msf_level = BiPhonSix.getMstructureLevel();
        Level mf_level = BiPhonSix.getMformLevel();
        Level pF_level = BiPhonSix.getPhoneticLevel();
        // NetworkGen sixLevelNetwork =
        // NetworkGen.addNewToGrammar(liaisonGrammar);

        // 2. Read in data pairs of SemFs and AudFs. Instantiate databank object
        // to keep track of longest common substrings.

        PfcData pfcData = PfcData.readFromFile(dataFilePath);
        //pfcData.outputToTable();
        PairDistribution pairDistribution = pfcData.getPairDistribution();

        lcsData = SubstringDatabank.createInstance();
        int maxUnfoundNgraph = config.getInt("gen.constrainers.maxUnfoundNgraph");
        int nGraphSize = config.getInt("gen.constrainers.ngraphSize");
        if (maxUnfoundNgraph > 0) {
            bigraphs = new ByteNGraphMap(nGraphSize);
            for (Form f : pairDistribution.getRightForms()) {
                bigraphs.addFromString(f.toString());
            }
        }


        // 3. Build forms outside-in:
        // SemF-->MStruc, MStruc --> MForm; save associations
        // between MForms and PhoneticForms as well!

        SynToMStructure sem_morph_gen = new SynToMStructure(semF_level, msf_level);
        MStructureToMForm ms_mf_gen = new MStructureToMForm(msf_level, mf_level);

        Set<FormMapping> semfMsMappings;
        for (FormPair gfp : pairDistribution.getKeySet()) {
            semfMsMappings = Sets.newHashSet();
            Form leftForm = gfp.left();
            semfMsMappings.addAll(sem_morph_gen.generateRight(leftForm).getContents());

            for (FormMapping mapping : semfMsMappings) {
                MStructure mStructure = (MStructure) mapping.right();
                SubCandidateSet msMfMappings = ms_mf_gen.generateRight(mStructure);
                for (FormMapping msMfMapping : msMfMappings) {
                    MForm mf = (MForm) msMfMapping.right();
                    lcsData.addPair(mf, (PhoneticForm) gfp.right());
                }

            }
        }

        lcsData.init();

        // 4. Generate UFs from MFs with the help of longest-common substring
        // data (or, alternatively, read from file)
        Multimap<MForm, PhoneticForm> MF_PF_mappings = lcsData.getMfToPf();
        Collection<MForm> allMforms = MF_PF_mappings.keySet();

        LexicalHypothesisRepository repository = new LexicalHypothesisRepository(lcsData);

            if (getLexiconFromFile) {
                String filename;
                if (config.hasPath("files.lexicon")) {
                    filename = config.getString("files.lexicon");
                } else {
                    filename = dataFilePath.replace(".txt",".lex");
                }
                StringMultimap stringMultimap = StringMultimap.readFromFile(filename);
                for (MForm mf : allMforms) {
                    for (MorphologicalWord mWord: mf) {
                        for (Morpheme morpheme: mWord) {
                            String mString = morpheme.toString();
                            Collection<String> pStrings = stringMultimap.get(mString);
                            if (pStrings.isEmpty()) {
                                //System.err.println("No morphs found for " + mString);
                                String altString = mString.replace(".num[SG]","");
                                altString = altString.replace(".num[PL]","");
                                pStrings = stringMultimap.get(altString);
                                if (pStrings.isEmpty()) {
                                    altString = altString.replace(".g[M]","").replace(".g[F]","");
                                    pStrings = stringMultimap.get(altString);
                                    if (pStrings.isEmpty()) {
                                        System.err.println("Still no morphs found for " +altString);
                                    }
                                }
                            }
                            for (String pString: pStrings) {
                                if (pString.equals("∅")) {
                                    repository.addFromString(morpheme, "");
                                }
                                else {
                                    repository.addFromString(morpheme, pString);
                                }
                            }
                        }
                    }
                }
            }
            else {
                for (MForm mf : allMforms) {
                Collection<PhoneticForm> pfs = MF_PF_mappings.get(mf);
                for (PhoneticForm pf : pfs) {
                    MorphemePhoneAligner mpa = new MorphemePhoneAligner(mf, pf.getContents(), lcsData);
                    Collection<MorphemePhoneAlignment> alignments = mpa.getAlignments();
                    for (MorphemePhoneAlignment alignment : alignments) {
                        repository.addAlignment(alignment);
                    }
                }
            }
        }
        if (config.getBoolean("gen.abstractEnabled")) {
            List<String> strings = config.getStringList("gen.abstractPhonemes");
            for (String string: strings) {
                String[] parts = string.split("~");
                repository.createAbstractForms(parts[0],parts[1]);
            }
        }
        if (config.getBoolean("gen.automaticSchwaAppendage")) {
                 repository.appendPosteriorSchwas();
        }

        repository.printContents();
        if (saveLexiconToFile) {
            String filename = dataFileName.replace(".txt",".lex");
            StringMultimap stringMultimap = repository.toStringMultimap();
            stringMultimap.writeToFile(outputPath+"/"+filename);
        }
        MFormToUF mf_uf_gen = new MFormToUF(repository);
        if (maxUnfoundNgraph > 0) {
            mf_uf_gen.addConstrainer(new UfConstrainer(bigraphs, nGraphSize));
        }
        System.out.println(mf_uf_gen.getLevels().getLeft());
        System.out.println(mf_uf_gen.getLevels().getRight());
        DynamicNetworkGrammar grammar = DynamicNetworkGrammar.createInstance(BiPhonSix.getLevelSpace(),
                "DynamicNetworkGrammar");
        grammar.addSubGen(sem_morph_gen);
        grammar.addSubGen(ms_mf_gen);
        grammar.addSubGen(mf_uf_gen);
        // UF gen;
        PredefinedUFToSF uf_sf_gen = new PredefinedUFToSF();
        grammar.addSubGen(uf_sf_gen);
        SFtoPF sf_pf_gen = new SFtoPF();
        grammar.addSubGen(sf_pf_gen);

        setCaching(grammar, mappingCacheSize);
        pairDistribution = pairDistribution.filter(new SinglesFilter());
        //pairDistribution = pairDistribution.squareRoot();

        if (config.getBoolean("grammar.useCandidateSpaces")) {
            System.out.println("Creating candidate spaces!");
            CandidateSpaces candidateSpaces;
            if (!config.getBoolean("grammar.loadCandidateSpaces")) {
                candidateSpaces = CandidateSpaces.fromDistribution(pairDistribution, grammar);

            } else {
                String nodesPath = dataFilePath.replace(".txt",".nodes");
                CandidateSpacesNodeList candidateSpacesNodeList = CandidateSpacesNodeList.readFromFile(nodesPath);
                candidateSpaces = candidateSpacesNodeList.toCandidateSpaces(pairDistribution,grammar);
            }
            grammar.addCandidateSpaces(candidateSpaces);

            if (config.getBoolean("grammar.writeCandidateSpaces")) {
                String dirName = "/candidate_spaces";
                String newDirPath = outputPath+dirName;
                File newDir = new File(newDirPath);
                boolean success = newDir.mkdir();
                if (success) {
                    CandidateSpacesToTables.writeToFile(candidateSpaces,newDirPath);
                }
            }
        }

        Timer timer = new Timer();

        System.out.println("Testing grammar on learning data...");
        GrammarTester.testGrammarOnLearningData(grammar, pairDistribution, 100, 1.0);
        timer.reportElapsedTime("Did tests in ", false);


        SettingsMap settingsMap = new SettingsMap();
        boolean randomLearner = false;
        for (String s: settingsMap.getMap().get("updateAlgorithm")) {
            if (s.equalsIgnoreCase("RandomBaseline")) {
                System.out.println("Doing random learning");
                randomLearner = true;
            }
        }

        PfcCooccurrences cooccurrences = pfcData.getCooccurrences();

        System.out.println("Expected error: " + pairDistribution.calculateExpectedError());
        if (randomLearner) {
            RandomLearnerTester randomLearnerTester = new RandomLearnerTester(grammar,pairDistribution, numEvaluations);
            randomLearnerTester.testAndPrint(numRuns);
        }
        else {
            LearningPropertyCombinations learningPropertyCombinations = LearningPropertyCombinations.fromMultimap(settingsMap.getMap(), grammar.getDefaultLearningProperties());

            // TODO Handle this better?

            TrajectoriesTester trajectoriesTester = new TrajectoriesTester(learningPropertyCombinations, grammar, pfcData);
            trajectoriesTester.testAndWrite(dataFileName, numEvaluations, numRuns, numThreads, outputPath);
//            Map<UUID,Hierarchy> successfulHierarchies = trajectoriesTester.getSuccesfulHierarchies();
            Table<SemSynForm, UUID, CandidateMappingTable> liaisonCandidates = trajectoriesTester.getLiaisonCandidates();
            if (!liaisonCandidates.isEmpty()) {
                List<RiverPlotOutput> riverPlotOutputs = Lists.newArrayList();

                for (SemSynForm ssf : liaisonCandidates.rowKeySet()) {
                    Collection<CandidateMappingTable> allCandidateTables = liaisonCandidates.row(ssf).values();
                    CandidateMappingTable merged = CandidateMappingTable.createFromCollection(allCandidateTables);
                    RiverPlotOutput asPlot = new RiverPlotOutput(ssf, merged, 0.01);
                    riverPlotOutputs.add(asPlot);

                }
                RiverPlotOutput.writeNodeAndEdgeFiles(outputPath + "/riverplot",riverPlotOutputs);
            }



                // check this out later

//            List<RiverPlotOutput> riverPlotOutputs = Lists.newArrayList();
//
//            for (int i = 1; i < 10; i++) {
//                for (FormPair fp : liaisonPairs) {
//                    CandidateMappingTable candidateMappingTable = CandidateMappingTable.createNew();
//                    RiverPlotOutput riverplot = new RiverPlotOutput(fp.left(),candidateMappingTable,0.05);
//                    Evaluation outcome = grammar.evaluate(fp, true, 1.0);
//                    candidateMappingTable.addCandidate(outcome.getWinner().getCandidate(), 1);
//                }
//            }

                // For printing
                Hierarchy best = trajectoriesTester.getBestHierarchy();
                SimpleTableauBuilder builder = new SimpleTableauBuilder(best);
                int count = 0;
                for (FormPair formPair : pairDistribution.getKeySet()) {
                    ViolatedCandidate winner = grammar.getWinner(formPair, false, 1);
                    builder.addViolatedCandidate(winner);
                    count++;
                    if (count > 40) {
                        break;
                    }
                }
                SimpleTableau tableau = builder.build();
                System.out.println(tableau.toSeparatedString("\t"));

            List<SemSynForm> ssfs = Lists.newArrayList();
            for (int i=0; i < 10; i++) {
                ssfs.add(cooccurrences.getUnattestedSsf());
            }
            for (SemSynForm ssf: ssfs) {
                Evaluation evaluation = grammar.evaluate(FormPair.createUnlabeled(ssf, Direction.RIGHT), false, 1.0);
                ViolatedCandidate winner = evaluation.getWinner();
                System.out.println(winner.getCandidate());
            }


            }



    }

    private static void preTestInputs(DynamicNetworkGrammar grammar, PairDistribution pairDistribution) {
        Set<FormPair> pairs = pairDistribution.getForLearningDirection(Direction.RIGHT);
        for (FormPair formPair : pairs) {
            grammar.evaluate(formPair, true, 1.0);
        }
    }

    /**
     * @param grammar
     * @param mappingCacheSize
     */
    private static void setCaching(DynamicNetworkGrammar grammar, int mappingCacheSize) {
        for (SubGen<?, ?> subgen : grammar.getSubGensByLevel()) {
            if (subgen != null) {
                subgen.addLoadingCache(mappingCacheSize);
            }
        }
    }

    /**
     * @param semF_level
     * @param pF_level
     * @return
     */
    private static PairDistribution createPairDistribution(
            String learningDataFileName, Level semF_level, Level pF_level) {
        DataPairReader dr = DataPairReader.createInstance(learningDataFileName, semF_level, pF_level);
        dr.readDistribution();
        dr.testReading();
        return dr.getPairDistribution();
    }

    private static void copyApplicationConf(String outputPath) {
        try {
            Path applicationConf = Paths.get(Resources.getResource("application.conf").toURI());
            Path targetPath = Paths.get(outputPath+"/application.conf");
            Files.copy(applicationConf,targetPath);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }



}
