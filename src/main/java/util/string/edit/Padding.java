/**
 *
 */
package util.string.edit;

/**
 * @author jwvl
 * @date May 11, 2015
 */
public class Padding {

    public static String right(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    public static String left(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }


}
