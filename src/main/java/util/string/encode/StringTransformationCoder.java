package util.string.encode;

/**
 * Created by janwillem on 23/12/2016.
 */
public class StringTransformationCoder {

    public static String getEncoding(String original, String transformed) {
        if (original.equals(transformed)) {
            return ("$EQ$");
        }
        String lcs = LongestCommonSubstring.getLongestCommonSubstring(original,transformed);
        String originalReplaced = original.replace(lcs,"§");
        String transformedReplaced = transformed.replace(lcs,"§");
        String[] originalSplit = getBeforeAfter(originalReplaced,"§");
        String[] transformSplit = getBeforeAfter(transformedReplaced,"§");
        String prePart = (originalSplit[0].equals(transformSplit[0]) ? "=" : originalSplit[0]+"]"+transformSplit[0]);
        String postPart = (originalSplit[1].equals(transformSplit[1]) ? "=" : originalSplit[1]+"["+transformSplit[1]);
        String result = prePart+"$"+postPart;
        return result;
    }

    public static void main(String[] args) {
        System.out.println(getEncoding("stofzuigen","gestofzuigd"));
    }

    private static String[] getBeforeAfter(String toSearch, String character) {
        String[] result = new String[2];
        int index =toSearch.indexOf(character);
        result[0] = toSearch.substring(0,index);
        result[1] = toSearch.substring(index+1, toSearch.length());
        return result;
    }
}
