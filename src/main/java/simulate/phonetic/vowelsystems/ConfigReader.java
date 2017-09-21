package simulate.phonetic.vowelsystems;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigObject;
import forms.primitives.feature.SurfaceRangeFeature;
import io.config.HoconUtils;
import io.vowels.ListTable;
import learn.data.PairDistribution;
import phonetics.DiscretizedScale;
import phonetics.Measure;
import simulate.phonetic.vowelsystems.data.PairDistributionReader;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigReader {

    public static Map<String, DiscretizedScale> getPhoneticScales(String configFile) {
        Map<String,DiscretizedScale> result = new LinkedHashMap<>();
        Config config = ConfigFactory.load(configFile);
        List<? extends ConfigObject> scaleObjects = config.getObjectList("grammar.phoneticScales");
        for (ConfigObject phoneticScaleObject: scaleObjects) {
            String unit = HoconUtils.getStringFromObject(phoneticScaleObject,"unit");
            String scale = HoconUtils.getStringFromObject(phoneticScaleObject,"scale");
            double min = HoconUtils.getDoubleFromObject(phoneticScaleObject,"min");
            double max = HoconUtils.getDoubleFromObject(phoneticScaleObject,"max");
            double stepSize = HoconUtils.getDoubleFromObject(phoneticScaleObject,"stepSize");
            Measure measure = new Measure(scale,unit);
            DiscretizedScale discretizedScale = new DiscretizedScale(measure,min,max,stepSize);
            result.put(unit,discretizedScale);
        }
        return result;
    }

    public static Map<String,List<SurfaceRangeFeature>> getSurfaceFeatures(String configFile) {
        Map<String,List<SurfaceRangeFeature>> result = new LinkedHashMap<>();
        Config config = ConfigFactory.load(configFile);
        List<? extends ConfigObject> scaleObjects = config.getObjectList("grammar.surfaceScales");
        for (ConfigObject surfaceScaleObject: scaleObjects) {
            String parent = HoconUtils.getStringFromObject(surfaceScaleObject,"parent");
            String name = HoconUtils.getStringFromObject(surfaceScaleObject,"name");
            List<String> valueNames = surfaceScaleObject.toConfig().getStringList("values");
            List<SurfaceRangeFeature> surfaceRangeFeatures = SurfaceRangeFeature.fromNameAndValueList(name,valueNames);
            result.put(parent,surfaceRangeFeatures);
        }
        return result;
    }


    public static List<String> getVowels(String configFile) {
        return ConfigFactory.load(configFile).getStringList("grammar.underlyingForms");
    }

    public static PairDistribution loadPairDistribution(String configFile, Map<String,DiscretizedScale> labelsToScales) {
        Config config = ConfigFactory.load(configFile);
        String tablePath = config.getString("data.inputFile");
        ListTable listTable = ListTable.readFromFile(tablePath);
        PairDistribution result = PairDistributionReader.createFromListTable(listTable, config, labelsToScales);
        return result;

    }
}
