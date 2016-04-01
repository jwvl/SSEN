/**
 *
 */
package util.arrays;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jwvl
 * @date 19/02/2016
 */
public class Range {
    private final int start;
    private final int end;

    /**
     * @param start
     * @param end
     */
    private Range(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public static Range of(int start, int end) {
        return new Range(start, end);
    }

    public static int[][] cartesianRanges(Range[] input) {
        List<Set<Integer>> setList = new ArrayList<Set<Integer>>(input.length);
        for (Range range : input) {
            setList.add(range.asSet(true));
        }
        Set<List<Integer>> listSet = Sets.cartesianProduct(setList);
        int[][] result = new int[listSet.size()][input.length];
        int counter = 0;
        for (List<Integer> intList : listSet) {
            int[] list = result[counter];
            for (int i = 0; i < intList.size(); i++) {
                list[i] = intList.get(i);
            }
            counter++;
        }
        return result;
    }

    public int[] toValues(boolean inclusive) {
        int maximum = inclusive ? end + 1 : end;
        int numItems = maximum - start;
        int[] result = new int[numItems];
        for (int i = 0; i < result.length; i++) {
            result[i] = start + i;
        }
        return result;

    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int size(boolean inclusive) {
        int difference = end - start;
        return inclusive ? difference + 1 : difference;
    }

    private Set<Integer> asSet(boolean inclusive) {
        int[] values = toValues(inclusive);
        Set<Integer> result = new HashSet<Integer>(values.length);
        for (int v : values) {
            result.add(v);
        }
        return result;

    }


}
