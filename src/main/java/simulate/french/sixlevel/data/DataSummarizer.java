package simulate.french.sixlevel.data;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import util.collections.FrequencyMap;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by janwillem on 09/05/2017.
 */
public class DataSummarizer {

    public static void main(String[] args) {
        String filename = "data/pfc/PFC_version04.txt";
        FrequencyMap<String> stringPairs;
        List<String> lines = Lists.newArrayList();
        URL url = Resources.getResource(filename);
        try {
            lines = Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
