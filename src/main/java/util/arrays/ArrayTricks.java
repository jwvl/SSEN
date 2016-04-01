/**
 *
 */
package util.arrays;

import java.util.LinkedList;
import java.util.List;

/**
 * @author jwvl
 * @date Nov 19, 2014
 */
public class ArrayTricks {
    public static int max(int[] numbers) {
        int result = Integer.MIN_VALUE;
        for (int i : numbers) {
            if (i > result)
                result = i;
        }
        return result;
    }

    public static String[] removeElement(String[] input, String deleteMe) {
        List<String> result = new LinkedList<String>();

        for (String item : input)
            if (!deleteMe.equals(item))
                result.add(item);

        return result.toArray(input);
    }

    public static boolean[] minIndices(int[] toTest) {
        boolean[] result = new boolean[toTest.length];
        // First run: get min
        int currMin = toTest[0];
        for (int i = 1; i < toTest.length; i++) {
            if (toTest[i] < currMin) {
                currMin = toTest[i];
            }
        }
        // second run: set TRUE values
        for (int i = 0; i < result.length; i++) {
            result[i] = (toTest[i] == currMin);
        }
        return result;
    }

    public static double getMinimum(double[] array) {
        double result = Double.MAX_VALUE;
        for (double d : array) {
            if (d < result) {
                result = d;
            }
        }
        return result;
    }

    public static double getMaximum(double[] array) {
        double result = Double.MIN_VALUE;
        for (double d : array) {
            if (d > result) {
                result = d;
            }
        }
        return result;
    }


}
