/**
 *
 */
package util.arrays;

import forms.primitives.segment.Phone;
import gen.rule.string.Side;

import java.util.Arrays;

/**
 * @author jwvl
 * @date Aug 29, 2015
 */
public class ByteArrayUtils {
    public static final byte[] EMPTY = new byte[0];

    public static int indexOf(byte[] toSearch, int startAt, byte... pattern) {
        int stopAt = toSearch.length - (pattern.length);
        for (int i = startAt; i <= stopAt; i++) {
            if (matches(toSearch, i, pattern)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean matches(byte[] toSearch, int atIndex, byte... pattern) {
        if (atIndex + pattern.length >= toSearch.length) {
            if (pattern.length > 0) {
                return false;
            } else return (atIndex == toSearch.length);
        } else if (atIndex < 0) {
            return false;
        }
        for (int i = 0; i < pattern.length; i++) {
            if (toSearch[atIndex + i] != pattern[i]) {
                return false;
            }
        }
        return true;
    }

    public static int indexOf(byte[] toSearch, byte... pattern) {
        return indexOf(toSearch, 0, pattern);
    }

    /**
     * @param beforeString
     * @return
     */
    public static byte[] fromString(String string) {
        byte[] result = new byte[string.length()];
        for (int i = 0; i < string.length(); i++) {
            result[i] = Phone.getInstance(string.charAt(i)).getId();
        }
        return result;
    }

    public static byte[] getSubArray(byte[] original, int from, int to) {
        int newLength = to - from;
        byte[] result = new byte[newLength];
        System.arraycopy(original, from, result, 0, newLength);
        return result;
    }

    /**
     * @param beforeString
     * @param maxLength
     * @return
     */
    public static byte[] fromStringPadded(String string, int maxLength) {
        byte[] result = new byte[maxLength];
        for (int i = 0; i < maxLength; i++) {
            byte toPut = i >= string.length() ? Phone.NULL.getId() : Phone.getInstance(string.charAt(i)).getId();
            result[i] = toPut;
        }
        return null;
    }

    public static byte[] padToLength(byte[] original, int newLength, Side padOnSide, byte padByte) {
        byte[] result = new byte[newLength];
        Arrays.fill(result, padByte);
        if (padOnSide == Side.LEFT) {
            int difference = newLength - original.length;
            System.arraycopy(original, 0, result, difference, original.length);
        } else {
            System.arraycopy(original, 0, result, 0, original.length);
        }
        return result;
    }

}
