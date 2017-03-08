package io.pfc.app;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by janwillem on 26/09/16.

 */
public class RepeatItemCollapser {
    private static boolean printWords = false;

    public static void main(String[] args) {

        Multiset<String> strings = HashMultiset.create();
        String liaisonsPath = "data/pfc/frenchExtraItemsWithApocope.txt";
        URL url = Resources.getResource(liaisonsPath);
        List<String> allLines;
        try {
            allLines = Resources.readLines(url, Charset.defaultCharset());
            for (String line: allLines) {
                String[] parts = line.split("\t");
                String word = parts[0].trim();
                int count = Integer.parseInt(parts[1]);
                strings.add(word,count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String s: strings.elementSet()) {
            System.out.println(s +"\t"+strings.count(s));
        }
    }
}
