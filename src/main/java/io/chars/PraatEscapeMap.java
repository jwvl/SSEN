/**
 *
 */
package io.chars;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jwvl
 * @date Dec 5, 2014
 */
public class PraatEscapeMap {
    private static Map<Character, String> escaperMap = createMap();

    /**
     * @return
     */
    private static Map<Character, String> createMap() {
        HashMap<Character, String> result = new HashMap<Character, String>();
        result.put(' ', " ");
        //TODO fill someday
        return null;
    }

}
