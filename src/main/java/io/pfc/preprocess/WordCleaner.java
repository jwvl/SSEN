package io.pfc.preprocess;

import com.google.common.base.CharMatcher;
import io.pfc.liaison.LiaisonNotationParser;

/**
 * Created by janwillem on 27/09/16.
 */
public class WordCleaner {
    private static CharMatcher matcher = CharMatcher.anyOf(" /:.,<>\"?!()");

    public static String cleanWord(String original) {
        //1 Take off liaison part
        int wordEnd = LiaisonNotationParser.findWordEnd(original);
        String noLiaison = original.substring(0,wordEnd);
        // 2 Clean off punctuation characters
        String cleaned = matcher.trimFrom(noLiaison);
        //String cleaned = noLiaison.replaceAll("^[^a-zA-Z0-9\\s]+|[^a-zA-Z0-9\\s]+$", "");
        return cleaned.toLowerCase();
    }
}
