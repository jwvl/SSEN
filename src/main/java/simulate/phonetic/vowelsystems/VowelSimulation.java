package simulate.phonetic.vowelsystems;


import com.google.common.collect.Lists;
import forms.primitives.feature.SurfaceRangeFeature;
import grammar.dynamic.DynamicNetworkGrammar;
import learn.batch.LearningTrajectory;
import learn.data.PairDistribution;
import phonetics.DiscretizedScale;
import simulate.phonetic.vowelsystems.data.ConfusionMatrixBuilder;
import simulate.phonetic.vowelsystems.levels.FeatureValueForm;
import simulate.phonetic.vowelsystems.levels.UnderlyingVowelForm;
import simulate.phonetic.vowelsystems.levels.VowelSimLevels;
import simulate.phonetic.vowelsystems.subgens.RangeFeatureFactory;
import simulate.phonetic.vowelsystems.subgens.UnderlyingFormFactory;
import simulate.phonetic.vowelsystems.subgens.VowelPfSfGen;
import simulate.phonetic.vowelsystems.subgens.VowelSfUfGen;
import util.collections.ConfusionMatrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by janwillem on 29/04/16.
 */
public class VowelSimulation {
    private static String configFile = "vowelSpaces.conf";

    public static void main(String[] args) {
        Map<String, DiscretizedScale> phoneticScales = ConfigReader.getPhoneticScales(configFile);
        Map<String,List<SurfaceRangeFeature>> surfaceScales = ConfigReader.getSurfaceFeatures(configFile);
        List<List<SurfaceRangeFeature>> surfaceScalesAsLists = Lists.newArrayList();
        for (String parent: surfaceScales.keySet()) {
            List<SurfaceRangeFeature> list = surfaceScales.get(parent);
            surfaceScalesAsLists.add(list);
            for (SurfaceRangeFeature feature: list) {
                System.out.println(feature);
            }
        }
        List<DiscretizedScale> scales = new ArrayList<>(phoneticScales.values());
        VowelPfSfGen vowelPfSfGen = new VowelPfSfGen(scales,surfaceScales);
        List<String> vowels = ConfigReader.getVowels(configFile);
        List<UnderlyingVowelForm> vowelForms = UnderlyingFormFactory.getUnderlyingForms(vowels);
        List<FeatureValueForm> surfaceForms = RangeFeatureFactory.getAllSfs();
        VowelSfUfGen vowelSfUfGen = new VowelSfUfGen(surfaceScalesAsLists, surfaceForms, vowelForms);
        DynamicNetworkGrammar grammar = DynamicNetworkGrammar.createInstance(VowelSimLevels.getLevelSpace(),"Vowels");
        grammar.addSubGen(vowelPfSfGen);
        grammar.addSubGen(vowelSfUfGen);
        PairDistribution pairDistribution = ConfigReader.loadPairDistribution(configFile,phoneticScales);
        LearningTrajectory trajectory = new LearningTrajectory(grammar,pairDistribution,200000);
        trajectory.launch(4);
        ConfusionMatrix result = ConfusionMatrixBuilder.fromPairDistribution(pairDistribution, grammar, 5000, 1.0);
        result.printToString();

    }

//    private static getString(ConfigObject configObject, String key) {
//        return configObject.get(key).render();
//    }

}
