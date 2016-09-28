package io.pfc;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import io.pfc.liaison.LiaisonNotationParser;
import io.pfc.liaison.LiaisonOpportunity;
import io.pfc.tag.TagFilter;

import java.util.List;
import java.util.Set;

/**
 * Created by janwillem on 26/09/16.
 */
public class SentenceDatum {
    private final String[] tokens;
    private final String[] posTags;
    private final MetaData metaData;
    private static Set<String> allPosTags = Sets.newHashSet();

    public SentenceDatum(String[] words, String[] posTags, MetaData metaData) {
        this.tokens = words;
        this.posTags = posTags;
        this.metaData = metaData;
    }

    public static SentenceDatum parseFromLine(String line, String separator, MaxentTagger tagger) {
        String[] firstSplit = line.split(separator);
        MetaData metaData = MetaData.parseFromString(firstSplit[0]);
        if (firstSplit.length < 2) {
            System.err.println("Line cannot be split: "+line);
        }
        String[] words = firstSplit[1].split(" ");
        String [] posTags = getPosTags(words, tagger);

        return new SentenceDatum(words, posTags, metaData);
    }

    private static String[] getPosTags(String[] words, MaxentTagger tagger) {
        StringBuilder sentenceBuilder = new StringBuilder();
        for (int i=0; i < words.length; i++) {
            String iString = words[i];
            int chopAt = LiaisonNotationParser.findWordEnd(iString);
            sentenceBuilder.append(iString.substring(0,chopAt));
            if ( i < words.length- 1) {
                sentenceBuilder.append(" ");
            }
        }
        String asSentence = sentenceBuilder.toString();
        String taggedString = tagger.tagString(asSentence);
        String[] roughTags = taggedString.split(" ");
        String[] result = new String[words.length];
        int tagIndex = 0;
        for (int i=0; i < words.length; i++) {
            for (int j=tagIndex; j < roughTags.length; j++) {
                String[] wordAndTag = roughTags[j].split("_");
                if (words[i].indexOf(wordAndTag[0]) >= 0) {
                    result[i] = wordAndTag[1];
                    allPosTags.add(wordAndTag[1]);
                    tagIndex = j+1;
                    break;
                }
            }
        }
        return result;

    }


    public String[] getTokens() {
        return tokens;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public List<LiaisonOpportunity> getLiaisonOpportunities() {
        List<LiaisonOpportunity> result = Lists.newArrayList();
        for (int i=0; i < tokens.length-1; i++) {
            if (LiaisonNotationParser.isLiaisonSite(tokens[i])) {
                LiaisonOpportunity found = LiaisonOpportunity.parseFromStrings(tokens[i],tokens[i+1],posTags[i],posTags[i+1]);
                result.add(found);
            }
        }
        return result;
    }

    public List<LiaisonOpportunity> getLiaisonOpportunitiesByTags() {
        List<LiaisonOpportunity> result = Lists.newArrayList();
        for (int i=0; i < tokens.length-1; i++) {
            if (TagFilter.filter(posTags[i],posTags[i+1])) {
                LiaisonOpportunity found = LiaisonOpportunity.parseFromStrings(tokens[i],tokens[i+1],posTags[i],posTags[i+1]);
                result.add(found);
            }
        }
        return result;
    }

    public String getSentenceAsString() {
        StringBuilder builder = new StringBuilder(tokens[0]);
        builder.append("_").append(posTags[0]);
        for (int i = 1; i < tokens.length; i++) {
            builder.append(" ").append(tokens[i]).append("_").append(posTags[i]);
        }
        return builder.toString();
    }

    public String[] getPosTags() {
        return posTags;
    }

    public static Set<String> getAllPosTags() {
        return allPosTags;
    }
}
