package io.pfc.app;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import util.collections.FrequencyMap;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by janwillem on 29/07/2017.
 */
public class LiaisonCounter {

    public static String liaisonsFile = "data/pfc/liaisons.txt";

    public static void main(String[] args) {
        FrequencyMap<String> liaisonsPerConversation = new FrequencyMap<>();
        Set<String> speakersAndFiles = Sets.newHashSet();
        List<String> liaisonLines = getLines(liaisonsFile);
        String pattern = "\\d{2}";
        Pattern pat = Pattern.compile(pattern);

        for (String line: liaisonLines) {
            String[] split = line.split(":",2);
            if (split.length > 1) {
                if (split[0].length() <= 16) {
                    String speakerAndFile = split[0].trim().replaceAll(" ", "");
                    speakersAndFiles.add(speakerAndFile);
                    int count = countOccurrences(pat, split[1]);
                    liaisonsPerConversation.add(speakerAndFile, count);
                }
            }
        }

        for (String speakerAndFile: speakersAndFiles) {
            String toPrint = speakerAndFile.replace(",","\t");
            System.out.println(toPrint+"\t"+liaisonsPerConversation.getCount(speakerAndFile));
        }
    }

    private static int countOccurrences(Pattern pat, String s) {
        int count = 0;
        Matcher matcher = pat.matcher(s);
        int from = 0;
        while(matcher.find(from)) {
            count++;
            from = matcher.start() + 1;
        }
        return count;
    }

    private static List<String> getLines(String resource) {
        List<String> result = Lists.newArrayList();
        URL url = Resources.getResource(resource);
        try {
            result = Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
