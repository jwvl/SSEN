package io.pfc.tag;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * Created by janwillem on 27/09/16.
 */
public class TagFilter {
    private static String[] allowed = {"DET_N","DET_NC","DET_NPP","ADJ_N","ADJ_NC","ADJ_NPP","N_ADJ","NC_ADJ","NPP_ADJ"};
    private final static Multimap<String,String> allowedAsMap = createAllowedAsMap();


    public static boolean filter(String firstPosTag, String secondPosTag) {
        return allowedAsMap.containsEntry(firstPosTag,secondPosTag);
    }

    private static Multimap<String, String> createAllowedAsMap() {
        Multimap<String,String> result = HashMultimap.create();
        for (String pair: allowed) {
            String[] split = pair.split("_");
            result.put(split[0],split[1]);
        }
        return result;
    }
}
