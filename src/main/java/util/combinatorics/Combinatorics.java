/**
 *
 */
package util.combinatorics;

import com.google.common.base.Strings;

/**
 * Contains various methods needed for some combinatory operations
 * on Forms or Subforms
 *
 * @author jwvl
 * @date 10/11/2014
 */
public class Combinatorics {

    public static void fillInt(int[] toFill, int currVal, int currIndex) {

        toFill[currIndex] = currVal;
        int left = toFill.length - currIndex;
        String pre = Strings.padEnd(">", currIndex, ' ');
        System.out.println(pre + "Putting in " + currVal + " at index " + currIndex);
        if (left > 1) {
            for (int i = currVal - 1; i >= left; i--) {
                fillInt(toFill.clone(), i, currIndex + 1);
            }
        }
        System.out.println(pre + "Done!");
        for (int j : toFill) {
            System.out.print(j + " ");
        }
        System.out.println();
    }

    public static int[] getLeftmostNonNegative(int[] input, int startAt) {
        for (int i = startAt; i < input.length; i++) {
            if (input[i] >= 0) {
                return new int[]{i, input[i]};
            }
        }
        return new int[]{-1, -1};
    }

    /**
     * Returns getNumSteps two int with index and value of 'leftmost nonzero' value
     * of an int array. Returns {-1,-1} is nothing can be found.
     *
     * @param input
     * @param startAt
     * @return
     */
    public static int[] getLeftmostNonZero(int[] input, int startAt) {
        for (int i = startAt; i < input.length; i++) {
            if (input[i] > 0) {
                return new int[]{i, input[i]};
            }
        }
        return new int[]{-1, -1};
    }


}
