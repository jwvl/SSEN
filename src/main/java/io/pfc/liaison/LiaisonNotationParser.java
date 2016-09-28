package io.pfc.liaison;

/**
 * Created by janwillem on 26/09/16.
 */
public class LiaisonNotationParser {
    private static String[] codes = {"10","20","11","21","12","22"};

    public static boolean isLiaisonSite(String word) {
        for (String code: codes) {
            if (word.contains(code)) {
                return true;
            }
        }
        return false;
    }

    public static String[] getWordAndConsonant(String word) {
        String[] result = new String[2];
        int wordEnd = findWordEnd(word);
        result[0] = word.substring(0,wordEnd);
        if (wordEnd+2 <  word.length()) {
            result[1] = word.substring(wordEnd+2);
        } else {
            result[1] = "";
        }
        return result;
    }

    public static int findWordEnd(String word) {
        for (String code: codes) {
            int foundIndex = word.indexOf(code);
            if (foundIndex > 0) {
                return foundIndex;
            }
        }
        return word.length();
    }


}
