/**
 *
 */
package util.combinatorics;

/**
 * @author jwvl
 * @date 10/11/2014
 */
public class CartesianSets {
    public static boolean[][] getCartesianBooleanSet(int size) {
        int numPossibilities = (int) Math.pow(2, size);
        boolean[][] result = new boolean[numPossibilities][size];
        for (int i = 0; i < numPossibilities; i++) {
            String asBinary = Integer.toBinaryString(i);
            // Add leading zeroes if necessary
            int numLeadingZeroes = size - asBinary.length();
            for (int j = 0; j < numLeadingZeroes; j++) {
                asBinary = "0" + asBinary;
            }
            boolean[] toAdd = binaryStringToBooleanArray(asBinary);
            result[i] = toAdd;
        }
        return result;
    }

    private static boolean[] binaryStringToBooleanArray(String binaryString) {
        int length = binaryString.length();
        boolean[] result = new boolean[length];
        char[] asCharArray = binaryString.toCharArray();
        for (int i = 0; i < length; i++) {
            if (asCharArray[i] == '0') {
                result[i] = false;
            } else if (asCharArray[i] == '1') {
                result[i] = true;
            } else {
                // TODO Throw some sort of exception instead of crashing out
                System.err.println("Illegal binary string " + binaryString);
            }
        }
        return result;
    }
}
