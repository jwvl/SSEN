package io.pfc.preprocess;

import com.google.common.collect.Maps;
import io.MyStringTable;
import io.pfc.wordlist.TranscriptionList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by janwillem on 27/09/16.
 */
public class FullDataToPairDistribution {
    private static String inputFile = "data/inputs/pfc/PFC_version02.txt";
    private static String outputFile = "data/inputs/pfc/PFC_pairs.txt";
    private static boolean extractSingles = true;
    private static String transcriptionsPath = "data/pfc/transcriptions.txt";

    public static void main(String[] args) throws IOException {
        String prefix = FullDataToPairDistribution.class.getClassLoader().getResource("").getFile();
        TranscriptionList transcriptionList = TranscriptionList.createFromFile(prefix+transcriptionsPath);
        Map<String,String> ssfToOrth = Maps.newHashMap();
        File out = new File(prefix+ outputFile);
        if (!out.exists()) {
            out.createNewFile();
        }

        FileWriter fw = new FileWriter(out.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        MyStringTable table = MyStringTable.fromFile(inputFile, true, "\t");
        int numRows = table.getNumRows();
        for (int i=0; i < numRows; i++) {
            String hasLiaison = table.getString(i,4);
            String pf = table.getString(i, 7);
            int freq = Integer.parseInt(table.getString(i,8));
            String ssf1 = table.getString(i,9);
            String ssf2 = table.getString(i,10);
            String orth1 = table.getString(i,0);
            String orth2 = table.getString(i,1);
            ssfToOrth.put(ssf1,orth1);
            ssfToOrth.put(ssf2,orth2);

            int liaisonW = hasLiaison.equals("TRUE") ? 1 : 0;
            String pfW = rewriteNasalVowels(pf);
            String ssfW = ssf1+", "+ssf2;
            String lineToWrite = String.format("%s\t%s\t%d\t%d",ssfW,pfW,freq,liaisonW);
            bw.write(lineToWrite);
            if (i < numRows-1) {
                bw.newLine();
            }
        }
        if (extractSingles) {
            for (String ssf: ssfToOrth.keySet()) {
                String orth = ssfToOrth.get(ssf);
                String phon = transcriptionList.getTranscription(orth);
                String phonRw = rewriteNasalVowels(phon);
                String headSsf = makeHeadSWord(ssf);
                String lineToWrite = String.format("\n%s\t%s\t1\t0",headSsf,phonRw);
                if (!ssf.isEmpty()) {
                    bw.write(lineToWrite);
                }
            }
        }

        bw.close();
    }

    private static String rewriteNasalVowels(String pfW) {
        pfW = pfW.replaceAll("ɔ̃","Q");
        pfW = pfW.replaceAll("ɑ̃","&");
        return pfW. replaceAll("ɛ̃","3");
    }

    private static String makeHeadSWord(String original) {
        if (!original.endsWith("*")) {
            return original+"*";
        }
        return original;
    }
}
