/**
 *
 */
package forms.primitives.segment;

import com.google.common.collect.Sets;
import forms.phon.Sonority;

import java.util.Arrays;
import java.util.Set;

/**
 * @author jwvl
 * @date 26/09/2015
 */
public class PhoneClass {
    private final byte[] contents;
    private final String stringRepresentation;

    /**
     * @param contents
     * @param stringRepresentation
     */
    private PhoneClass(byte[] contents, String stringRepresentation) {
        // Make sure all values are unique by converting to Set and back to array;
        // also, make sure set is sorted
        Set<Byte> asSet = Sets.newHashSet();
        for (byte b : contents) {
            asSet.add(b);
        }
        this.contents = new byte[asSet.size()];
        int i = 0;
        for (Byte b : asSet) {
            this.contents[i++] = b;
        }
        Arrays.sort(this.contents);
        this.stringRepresentation = stringRepresentation;
    }

    public static PhoneClass createFromBytes(byte[] contents, String stringRepresentation) {
        return new PhoneClass(contents, stringRepresentation);
    }

    public static PhoneClass createFromString(String contentsString, String stringRepresentation) {
        Phone[] asPhones = Phone.toPhoneArray(contentsString);
        byte[] asBytes = new byte[asPhones.length];
        for (int i = 0; i < asBytes.length; i++) {
            asBytes[i] = asPhones[i].getId();
        }
        return createFromBytes(asBytes, stringRepresentation);
    }

    public static PhoneClass createFromSonority(Sonority s, String stringRepresentation) {
        Set<Phone> phones = Phone.getInventory().getBySonority(s);
        byte[] bytes = new byte[phones.size()];
        int i = 0;
        for (Phone p : phones) {
            bytes[i++] = p.getId();
        }
        return createFromBytes(bytes, stringRepresentation);
    }

    /**
     * @return the contents
     */
    public byte[] getContents() {
        return contents;
    }

    /**
     * @return Size of this phone class
     */
    public int size() {
        return contents.length;
    }

    public boolean contains(byte b) {
        return (Arrays.binarySearch(contents, b) >= 0);
    }

    public boolean contains(Phone p) {
        return contains(p.getId());
    }


}
