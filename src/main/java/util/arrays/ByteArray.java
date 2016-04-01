/**
 *
 */
package util.arrays;

import forms.primitives.segment.Phone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Not yet sure this is useful!
 *
 * @author jwvl
 * @date May 25, 2015
 */
public class ByteArray {

    private final byte[] contents;


    public ByteArray(byte[] contents) {
        this.contents = Arrays.copyOf(contents, contents.length);
    }

    public int size() {
        return contents.length;
    }

    public ByteArray(Collection<Phone> phones) {
        this.contents = new byte[phones.size()];
        int i = 0;
        for (Phone p : phones) {
            contents[i++] = p.getId();
        }
    }

    public ByteArray(Phone... phones) {
        this.contents = new byte[phones.length];
        int i = 0;
        for (Phone p : phones) {
            contents[i++] = p.getId();
        }
    }

    public ByteArray getChanged(int index, byte newValue) {
        byte[] newContents = Arrays.copyOf(contents, contents.length);
        newContents[index] = newValue;
        return new ByteArray(newContents);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(contents);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof ByteArray))
            return false;
        ByteArray other = (ByteArray) obj;
        return Arrays.equals(contents, other.contents);
    }

    public List<Phone> convertToPhones() {
        List<Phone> result = new ArrayList<Phone>(size());
        for (byte b : contents) {
            result.add(Phone.getByCode(b));
        }
        return result;
    }

}
