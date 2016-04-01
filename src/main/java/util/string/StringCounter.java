/**
 *
 */
package util.string;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;

/**
 * @author jwvl
 * @date 10/11/2014
 */
public class StringCounter {

    /**
     * Count occurrences of b in a.
     * Source: https://code.google.com/p/guava-libraries/issues/detail?id=877
     *
     * @param a String to search
     * @param s String to count occurrences of
     * @return Number of occurrences
     */
    public static int count(String a, String s) {
        return Iterables.size(Splitter.on(s).split(a)) - 1;
    }

    public static int firstIndexOfSubstring(String a, String s, int from) {
        return (a.indexOf(s, from));
    }

    public static int[] getIndicesOfSubstring(String a, String s) {
        int[] result = new int[count(a, s)];
        if (result.length < 1) {
            return result;
        }
        int count = 0;
        int index = a.indexOf(s);
        while (index >= 0) {
            result[count++] = index;
            index = a.indexOf(s, index + 1);
        }
        return result;
    }

    public static int getLongestUninterrupted(String toSearch, String toCount, String resetter) {
        int count = 0;
        int nextHit = toSearch.indexOf(toCount, 0);
        int maxCount = 0;

        while (nextHit >= 0) {
            int nextResetter = toSearch.indexOf(resetter, nextHit);
            System.out.println(nextHit + "," + nextResetter);
            if (nextHit < nextResetter || nextResetter < 0) {
                count++;
                if (count > maxCount) {
                    maxCount = count;
                }
                nextHit = toSearch.indexOf(toCount, nextHit + 1);
            } else {
                count = 0;
                nextHit = toSearch.indexOf(toCount, nextResetter + 1);
            }
        }
        return maxCount;
    }


}
