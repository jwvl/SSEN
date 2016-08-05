/**
 *
 */
package simulate.french.sixlevel;

import com.google.common.collect.Sets;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import forms.Form;
import forms.FormPair;
import forms.morphosyntax.MForm;
import forms.morphosyntax.MStructure;
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
import learn.PairDistribution;
import learn.batch.combination.LearningPropertyCombinations;
import learn.batch.combination.TrajectoriesTester;
import simulate.french.sixlevel.helpers.LexicalHypothesisRepository;
import simulate.french.sixlevel.helpers.SettingsMap;
import simulate.french.sixlevel.subgens.*;
import util.debug.Timer;
import util.string.ngraph.NGraphMap;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author jwvl
 * @date Dec 17, 2014
 *
 * The main class for performing the simulations described in Chapter 5 of my thesis.
 * It has grown a bit messy and could do with some refactoring.
 */
public class SixLevelFrenchDynamic {

    private static SubstringDatabank lcsData;
    private static NGraphMap bigraphs;

    public static void main(String[] args) throws IOException {

        // 0 Read data.phones and properties
        Config config = ConfigFactory.load();
        int numThreads = config.getInt("system.numThreads");

        int mappingCacheSize = config.getInt("grammar.mappingCacheSize");
        int numEvaluations = config.getInt("learning.numEvaluations");

        String phoneInfoFileName = config.getString("files.phoneInfo");
        String dataFileName = config.getString("files.learningData");

        MyStringTable phonesTable = MyStringTable.fromFile(phoneInfoFileName, true, ",");
        String sonorityColumn = config.getString("implementation.sonorityType");
        Phone.getFromStringTable(phonesTable, "Phone", "String", sonorityColumn);


        // 1 Create grammar with levels.
        Level semF_level = BiPhonSix.getSemSynFormLevel();
        Level msf_level = BiPhonSix.getMstructureLevel();
        Level mff_level = BiPhonSix.getMformLevel();
        Level pF_level = BiPhonSix.getPhoneticLevel();
        // NetworkGen sixLevelNetwork =
        // NetworkGen.addNewToGrammar(liaisonGrammar);

        // 2. Read in data pairs of SemFs and AudFs. Instantiate databank object
        // to keep track of longest common substrings.
        PairDistribution pairDistribution = createPairDistribution(
                dataFileName, semF_level, pF_level);

        lcsData = SubstringDatabank.createInstance();

        bigraphs = NGraphMap.createInstance(2);
        for (Form f : pairDistribution.getRightForms()) {
            bigraphs.addAll(f.toString());
        }

        // 3. Build forms outside-in:
        // SemF-->MStruc, MStruc --> MForm; save associations
        // between MForms and PhoneticForms as well!

        SynToMStructure sem_morph_gen = new SynToMStructure(semF_level, msf_level);
        MStructureToMForm ms_mf_gen = new MStructureToMForm(msf_level, mff_level);

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
        // data
        Map<MForm, PhoneticForm> MF_PF_mappings = lcsData.getMfToPf();
        Collection<MForm> allMforms = MF_PF_mappings.keySet();

        LexicalHypothesisRepository repository = new LexicalHypothesisRepository(lcsData);
        for (MForm mf : allMforms) {
            PhoneticForm pf = MF_PF_mappings.get(mf);
            MorphemePhoneAligner mpa = new MorphemePhoneAligner(mf, pf.getContents(), lcsData);
            Collection<MorphemePhoneAlignment> alignments = mpa.getAlignments();
            for (MorphemePhoneAlignment alignment : alignments) {
                repository.addAlignment(alignment);
            }
        }

        repository.printContents();
        MFormToUF mf_uf_gen = new MFormToUF(repository);
        mf_uf_gen.addConstrainer(new UfConstrainer(bigraphs, 2));

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

        if (ConfigFactory.load().getBoolean("grammar.useCandidateSpaces")) {
            System.out.println("Creating candidate spaces!");
            CandidateSpaces candidateSpaces = CandidateSpaces.fromDistribution(pairDistribution, grammar);
            grammar.addCandidateSpaces(candidateSpaces);
        }

        Timer timer = new Timer();
        System.out.println("Testing grammar on learning data...");
        GrammarTester.testGrammarOnLearningData(grammar, pairDistribution, 100, 1.0);
        timer.reportElapsedTime("Did tests in ", false);


        SettingsMap settingsMap = new SettingsMap();
        LearningPropertyCombinations learningPropertyCombinations = LearningPropertyCombinations.fromMultimap(settingsMap.getMap(), grammar.getDefaultLearningProperties());

        TrajectoriesTester trajectoriesTester = new TrajectoriesTester(learningPropertyCombinations, grammar, pairDistribution);
        trajectoriesTester.testAndWrite("combinations",numEvaluations,10,numThreads);


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

}
