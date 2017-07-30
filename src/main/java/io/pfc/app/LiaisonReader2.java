package io.pfc.app;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import io.pfc.SimplerSentenceDatum;
import io.pfc.lemma.LexiqueEntry;
import io.pfc.lemma.LexiqueReader;
import io.pfc.liaison.LiaisonOpportunity;
import io.pfc.liaison.PhoneticRewriter;
import io.pfc.preprocess.StringsToSentenceData;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by janwillem on 19/04/2017.
 */
public class LiaisonReader2 {

    public static String interviewersToIgnore = "data/pfc/speakersToIgnore.txt";
    private static boolean printSingleWords = false;

    public static void main(String[] args) {
        int count = 0;
        int liaisonCount = 0;
        List<SimplerSentenceDatum> data = StringsToSentenceData.getAsList("data/pfc/liaisons.txt");
        LexiqueReader lexiqueReader = new LexiqueReader("data/pfc/lexique.txt");
        PhoneticRewriter rewriter = new PhoneticRewriter(null);
        Multimap<String,LexiqueEntry> lexiqueMap = lexiqueReader.readMap();
        Set<LexiqueEntry> uniqueLemmas = Sets.newHashSet();
        Set<String> toIgnore = getLinesAsSet(interviewersToIgnore);
        for (SimplerSentenceDatum datum: data) {
            String[] tokens = datum.getTokens();
            List<List<LexiqueEntry>> lexiqueEntryList = getLexiqueEntries(tokens, lexiqueMap);
            for (int i=0; i < tokens.length-1; i++) {
                LexiqueEntry[] pairOfInterest = getPairOfInterest(lexiqueEntryList.get(i), lexiqueEntryList.get(i+1));

                if (pairOfInterest.length > 0) {
                    String token1 = tokens[i];
                    String token2 = tokens[i+1];
                    String pos1 = pairOfInterest[0].getPos();
                    String pos2 = pairOfInterest[1].getPos();
                    String phon1 = pairOfInterest[0].getPhonString();
                    String phon2 = pairOfInterest[1].getPhonString();
                    LiaisonOpportunity opportunity = LiaisonOpportunity.parseFromStrings(token1, token2, pos1, pos2);
                    uniqueLemmas.add(pairOfInterest[0]);
                    uniqueLemmas.add(pairOfInterest[1]);
                    String phonString = rewriter.rewrite(opportunity, phon1, phon2);
                    String ssfString =pairOfInterest[0].getBiphonSsf()+", "+pairOfInterest[1].getBiphonSsf();
                    String speakerString = datum.getMetaData().getSpeaker();
                    String codeString = datum.getMetaData().getFullCode();
                    //System.out.print(datum.getMetaData()+"\t"+ token1 +" "+ token2+"\t"+ssfString+"\t"+phonString+"\t1\t");
                    if (toIgnore.contains(codeString)) {
                        System.out.print(ssfString + "\t" + phonString + "\t1\t");
                        count++;
                        if (opportunity.hasLiaison()) {
                            liaisonCount++;
                            System.out.println("1");
                        } else {
                            System.out.println("0");
                        }
                    }

                }
            }
        }
        if (printSingleWords) {
            for (LexiqueEntry lexiqueEntry : uniqueLemmas) {
                //System.out.println("--------\t"+ lexiqueEntry.getLemma()+"\t"+lexiqueEntry.getBiphonSsf()+"\t"+lexiqueEntry.getPhonString()+"\t0\t0");
                System.out.println(lexiqueEntry.getBiphonSsf() + "\t" + lexiqueEntry.getPhonString() + "\t1\t0");
            }
        }
        //System.out.println(count +" items, liaison: "+liaisonCount);

    }

    private static LexiqueEntry[] getPairOfInterest(List<LexiqueEntry> leftList, List<LexiqueEntry> rightList) {
        LexiqueEntry[] result = new LexiqueEntry[2];
        for (LexiqueEntry entry: leftList) {
            if (entry.isArticle()) {
                result[0] = entry;
            }
        }
        if (result[0] == null) {
            for (LexiqueEntry entry: leftList) {
                if (entry.isAdjective()) {
                    result[0] = entry;
                }
            }
        }
        if (result[0] == null) {
            return new LexiqueEntry[0];
        }
        char number = result[0].getPos().startsWith("ADJ:num") ? 'p' : result[0].getNumber();
        List<LexiqueEntry> nouns = rightList.stream().filter(le -> le.isNoun()).collect(Collectors.toList());
        if (nouns.isEmpty()) {
            return new LexiqueEntry[0];
        }
        if (nouns.size() > 1) {
            Optional<LexiqueEntry> found = nouns.stream().filter(l -> l.getNumber() == number).findFirst();
            if (found.isPresent()) {
                result[1] = found.get();
            } else {
                result[1] = nouns.get(0);
            }
        } else {
            result[1] = nouns.get(0);
        }
        return result;
    }

    private static List<List<LexiqueEntry>> getLexiqueEntries(String[] tokens, Multimap<String, LexiqueEntry> lexiqueMap) {
        List<List<LexiqueEntry>> result = Lists.newArrayList();
        for (String token: tokens) {
            List currentList = Lists.newArrayList();
            Collection<LexiqueEntry> aEntries = lexiqueMap.get(token.replaceAll("\\d+[A-Za-z]+",""));
            for (LexiqueEntry lexiqueEntry: aEntries) {
                if (lexiqueEntry.isArticle() || lexiqueEntry.isAdjective() || lexiqueEntry.isNoun()) {
                    currentList.add(lexiqueEntry);
                }
            }
            result.add(currentList);
        }
        return result;
    }

    private static Set<String> getLinesAsSet(String resource) {
        List<String> result = Lists.newArrayList();
        URL url = Resources.getResource(resource);
        try {
            result = Resources.readLines(url, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Sets.newHashSet(result);
    }

}
