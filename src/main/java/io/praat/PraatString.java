/**
 *
 */
package io.praat;

/**
 * @author jwvl
 * @date Nov 19, 2014
 */
public class PraatString {

    static String friendly(String input) {
        String doubleQuotes = input.replaceAll("\"", "\"\"");
        String noHashes = doubleQuotes.replaceAll("#", "\\\\# ");
        String noFingers = noHashes.replaceAll("☞", "");
        return noFingers;
    }
}
