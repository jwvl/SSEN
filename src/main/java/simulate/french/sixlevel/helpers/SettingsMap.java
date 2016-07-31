package simulate.french.sixlevel.helpers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Created by janwillem on 31/07/16.
 * Serves as a sort of "configuration file" for running the simulations
 * (to avoid the hassle of file i/o for that purpose)
 */
public class SettingsMap {
    private final Multimap<String,String> settings;
    private static String[] updateAlgorithms = {"WeightedUncancelled","AllUpHighDown"};
    private static String[] initialPlasticities = {"0.5","1.0","1.5","2.0","2.5"};
    private static String[] plasticityDecays = {"0.8","0.5"};

    public SettingsMap() {
        settings = HashMultimap.create();
        for (String updateAlgorithm: updateAlgorithms) {
            settings.put("updateAlgorithm",updateAlgorithm);
        }

        for (String initialPlasticity: initialPlasticities) {
            settings.put("initialPlasticity",initialPlasticity);
        }

        for (String plasticityDecay: plasticityDecays) {
            settings.put("plasticityDecay",plasticityDecay);
        }
    }

    public Multimap<String,String> getMap() {
        return settings;
    }
}
