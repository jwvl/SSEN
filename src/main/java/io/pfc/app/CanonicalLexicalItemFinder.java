package io.pfc.app;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.Resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

/**
 * Created by janwillem on 22/06/2017.
 */
public class CanonicalLexicalItemFinder {
    private static String inputFile = "data/inputs/pfc/PFC_version04-simpler.lex";
    private static String outputFile = "out/PFC_version04-simpler-canonical.lex";

    public static void main(String[] args) {
        Multimap<String,String> toCheck = HashMultimap.create();
        List<String> toSave = Lists.newArrayList();
        List<String> lines = Lists.newArrayList();
        URL url = Resources.getResource(inputFile);
        try {
            lines = Resources.readLines(url, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String line: lines) {
            String[] parts = line.split("\t");
            String morph = parts[0];
            String phon = parts[1];
            if (morph.endsWith("DET") || !morph.startsWith("“") || morph.endsWith("”_NUM") || morph.endsWith("”_ADJ")) {
                toSave.add(line);
            }
            if (morph.endsWith("_N")) {
                if (!morph.contains(".num[")) {
                    toCheck.put(morph,phon);
                }
            }
        }
        for (String morph: toCheck.keySet()) {
            Collection<String> phons = toCheck.get(morph);
            int shortest = 1000;
            for (String phon: phons) {
                if (phon.length() < shortest) {
                    shortest = phon.length();
                }
            }
            for (String phon: phons) {
                if (phon.length() == shortest) {
                    String fullLine = morph+"\t"+phon;
                    toSave.add(fullLine);
                }
            }
        }
        writeLines(toSave, outputFile);
    }

    private static void writeLines(List<String> lines, String fileName) {
        File outputFile = new File(fileName);
        try {
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileWriter fw = new FileWriter(outputFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < lines.size(); i++) {
                bw.write(lines.get(i));
                bw.newLine();
            }
            bw.close();
        } catch(IOException e) {
            System.err.println("Could not write to " +fileName+", error: " + e);
        }

    }
}
