package io.pfc.preprocess;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import io.pfc.MetaData;
import io.pfc.SimplerSentenceDatum;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by janwillem on 19/04/2017.
 */
public class StringsToSentenceData {

    private static String patternString = "<(.*?)>";
    private static Pattern p = Pattern.compile(patternString);
    public static List<SimplerSentenceDatum> getAsList(String inputFile) {

        List<SimplerSentenceDatum> result = Lists.newArrayList();
        URL url = Resources.getResource(inputFile);
        List<String> lines = Lists.newArrayList();
        try {
            lines = Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line: lines) {
            String[] parts = line.split(":",2);
            MetaData metaData = MetaData.parseFromString(parts[0]);
            String sentencesString = line.split(",",2)[1];
            List<String> sentences = getSentences(sentencesString);
            for (String sentence: sentences) {
                String tokenized = sentence.replaceAll(","," ,").replaceAll("\\."," .").replaceAll("\\?"," ?").replaceAll("\\!"," !");
                tokenized = tokenized.replaceAll("l'","l' ");
                SimplerSentenceDatum toAdd = new SimplerSentenceDatum(tokenized.split("\\s+",0), metaData);
                //System.out.println(toAdd);
                result.add(toAdd);
            }
        }
        return result;
    }

    private static List<String> getSentences(String part) {
        List<String> result = Lists.newArrayList();
        Matcher m = p.matcher(part);
        while (m.find()) {
            result.add(m.group(1).trim());
        }
        result.add(part.replaceAll(patternString,"").trim());
        return result;
    }
}
