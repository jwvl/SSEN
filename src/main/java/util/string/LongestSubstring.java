/**
 *
 */
package util.string;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jwvl
 * @date 09/11/2014
 */
public class LongestSubstring {
    private final ArrayList<String> longest;
    private final int maxK;
    private List<String> rest;
    private final String source;

    public static List<String> find(List<String> inputs) {
        return find(inputs, Integer.MAX_VALUE);
    }

    public static List<String> find(List<String> inputs, int maxK) {
        int shortestIndex = argMinLength(inputs);
        String shortest = inputs.get(shortestIndex);
        inputs.remove(shortestIndex);
        LongestSubstring result = new LongestSubstring(shortest, inputs,
                Integer.MAX_VALUE);
        result.search();
        return result.longest;
    }

    private static int argMinLength(List<String> strings) {
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).length() < result) {
                result = i;
            }
        }
        return result;
    }

    /**
     * Private constructor
     *
     * @param inputs
     */
    private LongestSubstring(String source, List<String> listToSearch, int maxK) {
        this.source = source;
        this.maxK = maxK;
        this.rest = listToSearch;
        this.longest = new ArrayList<String>(listToSearch.size());

    }

    private boolean isSubstring(String s) {
        for (String t : rest) {
            if (!t.contains(s)) {
                return false;
            }
        }
        return true;
    }

    private void search() {
        int srcLength = source.length();
        boolean foundLongest = false;
        for (int k = srcLength; k > 0; k--) {
            String[] foundStrings = getAllSubStringsOfLengthK(source, k);

            for (String found : foundStrings) {
                if (isSubstring(found)) {
                    foundLongest = true;
                    longest.add(found);
                }

            }
            if (foundLongest) {
                return;
            }

        }
        if (!foundLongest) {
            longest.add("");
        }

    }

    private String[] getAllSubStringsOfLengthK(String toSearch, int k) {
        int toSearchLength = toSearch.length();
        int numStrings = (toSearchLength - k) + 1;
        if (k > toSearch.length()) {
            return new String[0];
        }
        String[] result = new String[numStrings];
        for (int i = 0; i < numStrings; i++) {
            result[i] = toSearch.substring(i, i + k);
        }
        return result;
    }

}
