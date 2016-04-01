/**
 *
 */
package util.string;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author jwvl
 * @date May 30, 2015
 */
public class Regex {

    private static Map<Character, String> escapes;

    static {
        escapes = Maps.newHashMap();
        escapes.put('[', "\\[");
        escapes.put('{', "\\{");
        escapes.put('}', "\\}");
        escapes.put('(', "\\(");
        escapes.put(')', "\\)");
        escapes.put('.', "\\.");
        escapes.put('+', "\\+");
        escapes.put('*', "\\*");
        escapes.put('?', "\\?");
        escapes.put('^', "\\^");
        escapes.put('$', "\\$");
        escapes.put('|', "\\|");
    }

    public static String charToEscapeString(char c) {
        if (escapes.containsKey(c))
            return escapes.get(c);
        return String.valueOf(c);
    }

}
