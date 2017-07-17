/**
 *
 */
package util.arrays;

import forms.primitives.segment.Phone;
import forms.primitives.segment.PhoneSubForm;

import java.util.Arrays;

/**
 * @author jwvl
 * @date Aug 29, 2015
 */
public class ByteBuilder {

    private byte[] building;
    private int currentIndex;
    private static int DEFAULT_CAPACITY = 40;


    public ByteBuilder(int capacity) {
        this.building = new byte[capacity];
        this.currentIndex = 0;
    }

    public ByteBuilder() {
        this.building = new byte[DEFAULT_CAPACITY];
        this.currentIndex = 0;
    }

    public void append(byte... toAppend) {

        for (byte b : toAppend) {
            building[currentIndex++] = b;
        }

    }

    public byte[] build() {
        return Arrays.copyOfRange(building, 0, currentIndex);
    }

    public void appendExcept(byte[] toAppend, byte... skip) {
        for (byte b : toAppend) {
            appendExcept(b, skip);
        }
    }

    public void appendExcept(byte toAppend, byte... skip) {
        for (byte b : skip) {
            if (b == toAppend) {
                // Returns before appending!
                return;
            }
        }
        append(toAppend);
    }

    public void copySubset(byte[] original, int from, int to) {
        int length = to - from;
        System.arraycopy(original, from, building, currentIndex, length);
        currentIndex += length;
    }

    public int getCapacity() {
        return building.length;
    }

    public int size() {
        return currentIndex;
    }

    /**
     * @return
     */
    public ByteBuilder copy() {
        ByteBuilder copy = new ByteBuilder(this.getCapacity());
        System.arraycopy(building, 0, copy.building, 0, size());
        copy.currentIndex = currentIndex;
        // System.out.println("Copied");
        return copy;
    }

    public String toPhoneString() {
        return PhoneSubForm.createFromByteArray(build()).toString();
    }

    /**
     * @param beforeBytes
     * @param id
     * @return
     */
    public static byte[] copyExcept(byte[] beforeBytes, byte id) {
        ByteBuilder result = new ByteBuilder(beforeBytes.length);
        result.appendExcept(beforeBytes, id);
        return result.build();
    }

    public static byte[] replaceAtIndex(int index, byte[] beforeBytes, int toReplaceLength, byte[] replacement) {

        int capacity = beforeBytes.length + (replacement.length - toReplaceLength);
        byte[] newArray = new byte[capacity];
        try {
            System.arraycopy(beforeBytes, 0, newArray, 0, index);
            System.arraycopy(replacement, 0, newArray, index, replacement.length);
        } catch (ArrayIndexOutOfBoundsException a) {
            System.out.println("Cannot replace " +Phone.decode(replacement) + " at index " + index + "for bytestring " + Phone.decode(beforeBytes));
        }
        int sourceIndex = index + toReplaceLength;
        int destIndex = index + replacement.length;
        int restSize = beforeBytes.length - sourceIndex;
        System.arraycopy(beforeBytes, sourceIndex, newArray, destIndex, restSize);
        return newArray;
    }

    public static byte[] flattenArrays(byte[]... inputs) {
        int totalSize = 0;
        for (byte[] input : inputs) {
            totalSize += input.length;
        }
        byte[] result = new byte[totalSize];
        int totalCounter = 0;
        for (byte[] input : inputs) {
            for (byte b : input) {
                result[totalCounter++] = b;
            }
        }
        return result;
    }

}
