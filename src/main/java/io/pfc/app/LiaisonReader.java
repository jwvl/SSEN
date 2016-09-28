package io.pfc.app;

import com.google.common.collect.Sets;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import io.pfc.DataParser;
import io.pfc.SentenceDatum;
import io.pfc.liaison.LiaisonOpportunity;
import io.pfc.liaison.PhoneticRewriter;
import io.pfc.ngrams.Bigram;
import io.pfc.ngrams.TwoWayBigramMap;
import io.pfc.preprocess.WordCleaner;
import io.pfc.wordlist.TranscriptionList;
import util.collections.FrequencyMap;

import java.util.List;
import java.util.Set;

/**
 * Created by janwillem on 26/09/16.
 * Used for prying relevant bigrams from the transcriptions of the PFC and
 * outputting them to a format (nearly) suitable to serve as input for the simulation framework.
 */
public class LiaisonReader {
    private static boolean printWords = false;

    public static void main(String[] args) {

        String liaisonsPath = "data/pfc/liaisons.txt";
        String transcriptionsPath = "data/pfc/transcriptions.txt";
        String taggerPath = "data/pfc/french.tagger";
        String prefix = LiaisonReader.class.getClassLoader().getResource("").getFile();
        MaxentTagger frenchTagger = new MaxentTagger(taggerPath);
        DataParser parser = DataParser.createFromPath(prefix+liaisonsPath, frenchTagger);
        TranscriptionList transcriptionList = TranscriptionList.createFromFile(prefix+transcriptionsPath);
        PhoneticRewriter phoneticRewriter = new PhoneticRewriter(transcriptionList);
        List<SentenceDatum> sentenceData = parser.getSentenceData();
        // Step one: read all pairs implicated in liaison
        Set<String> allWords = Sets.newTreeSet();
        FrequencyMap<LiaisonOpportunity> liaisons = new FrequencyMap<>();
        FrequencyMap<String> stringFrequencyMap = new FrequencyMap<>();
        TwoWayBigramMap bigrams = TwoWayBigramMap.createNew();
        int opportunityCounter = 0;
        for (SentenceDatum sentenceDatum: sentenceData) {
            String sentence = sentenceDatum.getSentenceAsString();
            for (String word: sentenceDatum.getTokens()) {
                String cleaned = WordCleaner.cleanWord(word);
                stringFrequencyMap.addOne(cleaned);
            }
            //System.out.println(sentence);
            for (LiaisonOpportunity liaisonOpportunity: sentenceDatum.getLiaisonOpportunitiesByTags()) {
                String leftWordOrth = liaisonOpportunity.getFirstWordOrth();
                String rightWordOrth = liaisonOpportunity.getSecondWordOrth();
                allWords.add(leftWordOrth);
                allWords.add(rightWordOrth);
                Bigram bigram = new Bigram(leftWordOrth,rightWordOrth);
                bigrams.addBigram(bigram);
                opportunityCounter++;
                liaisons.addOne(liaisonOpportunity);
                //System.out.println(opportunityCounter+": "+liaisonOpportunity.getFirstWordOrth()+" "+liaisonOpportunity.getSecondWordOrth()+"\t"+phoneticRewriter.rewrite(liaisonOpportunity));
            }
        }
        if (printWords) {
            System.out.println("\n\nWords found in corpus:");
            int charCounter = 0;
            for (String s : allWords) {
                charCounter += s.length() + 1;
                System.out.println(s);
                if (charCounter > 7000) {
                    System.out.println("\n\n");
                    charCounter = 0;
                }
            }
        }

        System.out.println("\nBigrams of interest:\n");

        for (LiaisonOpportunity liaisonOpportunity: liaisons) {
            int count = liaisons.getCount(liaisonOpportunity);
            if (count > 1 || liaisonOpportunity.hasLiaison()) {
                String phoneticRewrite = phoneticRewriter.rewrite(liaisonOpportunity);
                if (!phoneticRewrite.contains("null")) {
                    System.out.println(liaisonOpportunity + "\t" + phoneticRewriter.rewrite(liaisonOpportunity) + "\t" + count);
                }
                }
        }

//        System.out.println("Word frequencies:");
//        for (String word: stringFrequencyMap) {
//            int count = stringFrequencyMap.getCount(word);
//            System.out.println(word+"\t"+count);
//        }

    }
}
