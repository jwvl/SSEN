package simulate.phonetic.vowelsystems.data;

import com.google.common.collect.Lists;
import com.typesafe.config.Config;
import forms.primitives.feature.ScaleFeature;
import io.vowels.ListTable;
import learn.data.PairDistribution;
import phonetics.DiscretizedScale;
import simulate.phonetic.vowelsystems.levels.PhoneticValueForm;
import simulate.phonetic.vowelsystems.levels.UnderlyingVowelForm;
import simulate.phonetic.vowelsystems.subgens.UnderlyingFormFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PairDistributionReader {

    public static PairDistribution createFromListTable(ListTable listTable, Config config, Map<String, DiscretizedScale> labelsToScales) {
        PairDistribution result = new PairDistribution("data");
        Random random = new Random();
        double noise = config.getDouble("data.noise");
        int samplesPerRow = config.getInt("data.samplesPerRow");
        String labelcolumn = config.getString("data.labelColumn");
        List<String> dataColumns = Lists.newArrayList(labelsToScales.keySet());
        for (int i = 0; i < listTable.numRows(); i++) {

            String vowelLabel = listTable.getString(i, labelcolumn);
            UnderlyingVowelForm underlyingVowelForm = UnderlyingFormFactory.getUnderlyingForm(vowelLabel);
            for (int j = 0; j < samplesPerRow; j++) {
                List<ScaleFeature> features = Lists.newArrayList();
                for (String dataColumn : dataColumns) {

                    double value = listTable.getDouble(i, dataColumn);
                    DiscretizedScale scale = labelsToScales.get(dataColumn);
                    double sd = scale.getStepSize() * noise;

                    double noisedValue = value + random.nextGaussian() * sd;
                    ScaleFeature feature = scale.getForValue(noisedValue);
                    features.add(feature);
                }
                Collections.sort(features);
                PhoneticValueForm phoneticValueForm = new PhoneticValueForm(features);
                result.addOne(phoneticValueForm,underlyingVowelForm);
            }
        }
        return result;
    }
}
