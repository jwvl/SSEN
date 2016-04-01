/**
 *
 */
package util.arrays;


import org.junit.Test;

import java.util.Arrays;

import static junit.framework.Assert.assertTrue;

/**
 * @author jwvl
 * @date Aug 29, 2015
 */
public class ByteArrayUtilsTest {

    @Test
    public void searchSubArray() {
        byte[] toSearch = {0, 1, 2, 3, 5, 8, 13};
        byte single = 3;
        byte[] patternOne = {1, 2, 3};
        byte[] patternTwo = {1, 3, 2};
        assertTrue(ByteArrayUtils.indexOf(toSearch, single) == 3);
        assertTrue(ByteArrayUtils.indexOf(toSearch, patternOne) >= 0);
        assertTrue(ByteArrayUtils.indexOf(toSearch, patternTwo) < 0);
        assertTrue(ByteArrayUtils.indexOf(toSearch, toSearch) == 0);

    }

    @Test
    public void byteBuilderReplace() {
        byte[] original = {0, 1, 2, 2, 1, 1};
        int bytesToReplace = 2;
        byte[] replacement = {};
        byte[] result = ByteBuilder.replaceAtIndex(1, original, 2, replacement);
        System.out.println(Arrays.toString(result));
    }

}
