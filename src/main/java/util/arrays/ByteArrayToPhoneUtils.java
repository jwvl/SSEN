/**
 *
 */
package util.arrays;

import forms.primitives.segment.Phone;

import java.util.*;

/**
 * Not yet sure this is useful!
 *
 * @author jwvl
 * @date May 25, 2015
 */
public class ByteArrayToPhoneUtils {

    public static byte[] arrayFromPhoneCollection(Collection<Phone> phones) {
        byte[] result = new byte[phones.size()];
        int i = 0;
        for (Phone p : phones) {
            result[i++] = p.getId();
        }
        return result;
    }

    public static byte[] arrayFromPhones(Phone... phones) {
        byte[] result = new byte[phones.length];
        int i = 0;
        for (Phone p : phones) {
            result[i++] = p.getId();
        }
        return result;
    }

    public static List<Phone> arrayToPhonesList(byte[] input) {
        List<Phone> result = new ArrayList<Phone>(input.length);
        for (byte b : input) {
            result.add(Phone.getByCode(b));
        }
        return result;
    }

    public static Set<Phone> arrayToPhonesSet(byte[] input) {
        Set<Phone> result = new HashSet<Phone>(input.length);
        for (byte b : input) {
            result.add(Phone.getByCode(b));
        }
        return result;
    }

    /*
     * (c) user Wayne Uroda on Stack Overflow
     * http://stackoverflow.com/questions/5513152
     * /easy-way-to-concatenate-two-byte-arrays
     */
    public static byte[] concat(byte[]... arrays) {
        // Determine the length of the result array
        int totalLength = 0;
        for (int i = 0; i < arrays.length; i++) {
            totalLength += arrays[i].length;
        }

        // create the result array
        byte[] result = new byte[totalLength];

        // copy the source arrays into the result array
        int currentIndex = 0;
        for (int i = 0; i < arrays.length; i++) {
            System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
            currentIndex += arrays[i].length;
        }

        return result;
    }

}
