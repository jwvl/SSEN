/**
 *
 */
package util.string.edit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTestHarness {

    public static void main(String[] args) {

        Pattern pattern =
                Pattern.compile("()(\\+vw)()");

        Matcher matcher =
                pattern.matcher("bɔt+vwatyr");
        System.out.println("Pattern is: " + pattern.toString());

        boolean found = false;
        while (matcher.find()) {
            System.out.format("I found the text" +
                            " \"%s\" starting at " +
                            "index %d and ending at index %d.%n",
                    matcher.group(), matcher.start(), matcher.end());
            int nGroups = matcher.groupCount();
            for (int i = 0; i < nGroups; i++) {
                System.out.printf("Group %d: %s %n", i, matcher.group(i));
            }
            found = true;
        }
        if (!found) {
            System.out.format("No match found.%n");
        }

    }
}
