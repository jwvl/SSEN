package io.pfc;

import com.google.common.collect.Lists;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import io.pfc.liaison.LiaisonOpportunity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

/**
 * Created by janwillem on 26/09/16.
 * Reads
 */
public class DataParser {
    private final File file;
    private final int stopAtDatum;
    private final List<SentenceDatum> sentenceData;
    private final MaxentTagger tagger;

    private DataParser(File file, int stopAtDatum, List<SentenceDatum> sentenceData, MaxentTagger tagger) {
        this.file = file;
        this.stopAtDatum = stopAtDatum;
        this.sentenceData = sentenceData;
        this.tagger = tagger;
        parse();
    }

    private void parse() {
        int parseCount = 0;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String firstLine = reader.readLine();
            String nextLine;
            while ((nextLine = reader.readLine()) != null && parseCount < stopAtDatum) {
                parseCount++;
                if (!nextLine.isEmpty()) {
                    SentenceDatum nextDatum = SentenceDatum.parseFromLine(nextLine, ":", tagger);
                    sentenceData.add(nextDatum);
                    List<LiaisonOpportunity> liaisons = nextDatum.getLiaisonOpportunities();
                    for (LiaisonOpportunity liaisonOpportunity : liaisons) {
                        System.out.println(liaisonOpportunity);
                    }
                }
            }
            System.out.println("Done parsing " +parseCount +" lines");
            reader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static DataParser createFromPath(String path, MaxentTagger tagger, int stopAt) {
        List<SentenceDatum> dataList = Lists.newArrayList();
        File file = new File(path);
        return new DataParser(file, stopAt, dataList, tagger);
    }

    public static DataParser createFromPath(String path, MaxentTagger tagger) {
        return createFromPath(path, tagger, Integer.MAX_VALUE);
    }

    public List<SentenceDatum> getSentenceData() {
        return sentenceData;
    }
}
