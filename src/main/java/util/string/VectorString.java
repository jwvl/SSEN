/**
 *
 */
package util.string;

/**
 * @author jwvl
 * @date Nov 19, 2014
 */
public class VectorString {

    public static String toSeparatedString(int[] array, String separator) {
        StringBuilder result = new StringBuilder();
        result.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            result.append(separator);
            result.append(array[i]);
        }
        return result.toString();
    }

}
